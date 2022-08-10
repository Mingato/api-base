package com.netagentciadigital.api.commons.security;

import java.util.Collection;

import com.netagentciadigital.api.model.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;



public class AppUserSystem extends User{

    private static final long serialVersionUID = 5L;

    private final AppUser appUser;

    public AppUserSystem(AppUser appUser, Collection<? extends GrantedAuthority> authorities) {
        super(appUser.getEmail(), appUser.getPassword(), authorities);
        this.appUser = appUser;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

}
