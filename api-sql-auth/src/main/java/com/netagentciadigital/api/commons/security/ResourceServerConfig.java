package com.netagentciadigital.api.commons.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;


@Configuration
@EnableWebSecurity
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)//abilitar seguranca nos metodos
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        //gerar direto na memoria
        //auth.inMemoryAuthentication().withUser("redAdminApi").password("{noop}pass_api_wm3@p15re8%_super").roles("SUPER").authorities("SUPER");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .mvcMatchers("/.well-known/jwks.json").anyRequest()
                //.antMatchers("/.well-known/jwks.json").a
                .and()
                .authorizeRequests()
                .antMatchers("/","/pages/login.html", "/ambientes.json", "/.well-known/jwks.json").permitAll()
                .anyRequest().authenticated()//fazer todas as urls passarem por authenticacao
                .and()
                .headers()
                .frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();

    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.stateless(true);
    }


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();//permite fazer a seguranca nos metodos
    }

}