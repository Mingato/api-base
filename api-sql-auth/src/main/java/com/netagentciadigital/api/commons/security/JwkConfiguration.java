package com.netagentciadigital.api.commons.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

@Configuration
@Slf4j
public class JwkConfiguration {

    @Autowired
    private MyKeyPairGenerator myKeyPairGenerator;

    @Bean
    public KeyPair keyPairBean() throws NoSuchAlgorithmException {
        return myKeyPairGenerator.generateKeyPair();
    }
}
