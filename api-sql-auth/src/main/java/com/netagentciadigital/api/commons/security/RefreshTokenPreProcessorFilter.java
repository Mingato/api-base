package com.netagentciadigital.api.commons.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Administrador
 *	Antes de fazer a requisicao, pega o token do cookie para poder ser usado
 */
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenPreProcessorFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String refreshToken = "";
        HttpServletRequest req  = ((HttpServletRequest) request);//.getServletRequest();
        HttpServletResponse resp  = ((HttpServletResponse) response);

        log.info(req.getMethod() +" " + req.getRequestURI() +
                (req.getQueryString() != null ? "?" + req.getQueryString(): ""));

        if(req.getRequestURI().contains("/oauth/token")
                && "refresh_token".equalsIgnoreCase(req.getParameter("grant_type"))
                && req.getCookies() != null) {

            for(Cookie cookie : req.getCookies()) {
                if(cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }

            req = new MyServletRequestWrapper(req, refreshToken);
        }else if(!req.getRequestURI().contains("/login.html") &&
                !req.getRequestURI().contains("/actuator/") &&
                !req.getRequestURI().contains("/jwks.json")){

            String accessToken = null;

            if(req.getCookies() != null) {
                for(Cookie cookie : req.getCookies()) {
                    if(cookie.getName().equals("accessToken")) {
                        accessToken = cookie.getValue();
                    }
                    if(cookie.getName().equals("refreshToken")) {
                        refreshToken = cookie.getValue();
                    }
                }
            }

            if(accessToken != null) {
                req = new MyServletRequestWrapper(req, refreshToken, accessToken);
            }

        }

        chain.doFilter(req, resp);
    }

    public JwtAccessTokenConverter accessTokenConverter() {
        return new JwtAccessTokenConverter();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    /**
     *como o getParameter map nao pode ser mudado, entao esta classe é criada para altera-lo
     */
    static class MyServletRequestWrapper extends HttpServletRequestWrapper {
        private final String refreshToken;
        private String accessToken;

        private final Map<String, String> headerMap = new HashMap<>();

        public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
            super(request);
            this.refreshToken = refreshToken;
        }

        public MyServletRequestWrapper(HttpServletRequest request, String refreshToken, String accessToken) {
            super(request);
            this.refreshToken = refreshToken;
            this.accessToken = accessToken;
            addHeader("Authorization", "Bearer " + accessToken);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
            map.put("refresh_token", new String[] {refreshToken});
            map.put("Authorization", new String[] {"Bearer " + accessToken});
            map.setLocked(true);
            return map;
        }

        //////////HEADERS///

        public void addHeader(String name, String value) {
            headerMap.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = super.getHeader(name);
            if (headerMap.containsKey(name)) {
                headerValue = headerMap.get(name);
            }
            return headerValue;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            names.addAll(headerMap.keySet());
            return Collections.enumeration(names);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> values = Collections.list(super.getHeaders(name));
            if (headerMap.containsKey(name)) {
                values.add(headerMap.get(name));
            }
            return Collections.enumeration(values);
        }
    }
}
