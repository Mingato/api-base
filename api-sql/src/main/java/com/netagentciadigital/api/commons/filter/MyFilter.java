package com.netagentciadigital.api.commons.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req  = ((HttpServletRequest) servletRequest);
        HttpServletResponse resp  = ((HttpServletResponse) servletResponse);

        log.info(req.getMethod() +" " + req.getRequestURI() +
                (req.getQueryString() != null ? "?" + req.getQueryString(): ""));

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
