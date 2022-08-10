package com.netagentciadigital.api.commons.utils;

import com.netagentciadigital.api.model.AppUser;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfos {

    public static AppUser getUser(Authentication authentication){
        return AppUser.builder()
                .id(getUserId(authentication))
                .email(getEmail(authentication))
                .name(getName(authentication))
                .build();
    }

    public static String getEmail(Authentication authentication) {
        return decodeToken(authentication).get("user_name").toString();
    }

    public static String getUserId(Authentication authentication) {
        return decodeToken(authentication).get("id").toString();
    }

    public static String getName(Authentication authentication) {
        return decodeToken(authentication).get("name").toString();
    }

    public static List<String> getGroups(Authentication authentication) {
        return (List<String>) decodeToken(authentication).get("codigosGrupo");
    }

    public static List<String> getPerfis(Authentication authentication) {
        return (List<String>) decodeToken(authentication).get("codigosPerfil");
    }

    public static Map<String, Object> decodeToken(Authentication authentication){
        Map<String, Object> payload = new HashMap<>();

        String token = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();

        try {
            JWT jwt = JWTParser.parse(token);

            payload = ((SignedJWT) jwt).getPayload().toJSONObject();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return payload;
    }

    public static String getAccessToken(Authentication authentication){
        return ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
    }

    public static List<String> getAuthorities(Authentication authentication){
        return (List<String>) decodeToken(authentication).get("authorities");
    }
    public static List<String> getAuthorities(String token){
        return (List<String>) decodeToken(token).get("authorities");
    }

    public static Map<String, Object> decodeToken(String token){
        Map<String, Object> payload = new HashMap<>();

        try {
            JWT jwt = JWTParser.parse(token);

            payload = ((SignedJWT) jwt).getPayload().toJSONObject();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return payload;
    }


}
