package com.netagentciadigital.api.commons.security;

import java.util.HashMap;
import java.util.Map;

import com.redcompany.wm3.domain.Usuario;
import com.redcompany.wm3.domain.repositorio.RepositorioDeUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;


public class CustomTokenEnhancer implements TokenEnhancer{

    @Autowired
    private RepositorioDeUsuario repositorioDeUsuario;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Usuario usuario = repositorioDeUsuario.findByEmail(((User)authentication.getPrincipal()).getUsername());

        Map<String, Object> addInfo = new HashMap<>();
        addInfo.put("id", usuario.getId());
        addInfo.put("nome", usuario.getNome());
        addInfo.put("codigosPerfil", usuario.getCodigosPerfil());
        addInfo.put("codigosGrupo", usuario.getCodigosGrupo());
        addInfo.put("editorDeNoticia", usuario.getEditorDeNoticia());
        addInfo.put("urlImagem", usuario.getUrlImagem());
        addInfo.put("iss", "wm3@redcompany.com.br");
        addInfo.put("sub", "wm3@redcompany.com.br");


        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);

        return accessToken;
    }

}
