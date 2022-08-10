package com.netagentciadigital.api.commons.security;

import com.netagentciadigital.api.model.AppUser;
import com.netagentciadigital.api.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;


public class CustomTokenEnhancer implements TokenEnhancer{

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        AppUser usuario = appUserRepository.findByEmail(((User)authentication.getPrincipal()).getUsername());

        Map<String, Object> addInfo = new HashMap<>();
        addInfo.put("id", usuario.getId());
        addInfo.put("name", usuario.getName());
        addInfo.put("iss", "app@net-digital.com.br");
        addInfo.put("sub", "wm3@net-digital.com.br");


        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);

        return accessToken;
    }

}
