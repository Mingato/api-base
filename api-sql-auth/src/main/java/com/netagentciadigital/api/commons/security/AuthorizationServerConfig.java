package com.netagentciadigital.api.commons.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.KeyPair;
import java.util.Arrays;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private KeyPair keyPair;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${application.client1.client-id}")
    private String clientId1;

    @Value("${application.client1.client-secret}")
    private String clientSecret1;



    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()
                .withClient(clientId1)
                .secret("{noop}"+clientSecret1)
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(60 * 15)//15 minutos
                .refreshTokenValiditySeconds(3600 * 24 * 10)//dez dias
                .redirectUris("/pages/login.html");

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()").tokenKeyAccess("permitAll()");

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancedChain = new TokenEnhancerChain();// para passar algumas informacoes do usuario no token
        tokenEnhancedChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));

        endpoints
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancedChain)
                .accessTokenConverter(accessTokenConverter())//tipo do token
                .reuseRefreshTokens(false)//um novo refresh token e enviado ao pegar um token para sempre ter um token de refresh token
                .authenticationManager(authenticationManager);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(this.keyPair);
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {//aonde o token fica armazenado
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

}
