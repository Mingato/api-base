package com.netagentciadigital.api.commons.security;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


import com.netagentciadigital.api.model.AppUser;
import com.netagentciadigital.api.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


@Component
public class UsersLogged {

    @Autowired
    private AppUserService appUserService;

    private final List<AppUser> logados = Lists.newArrayList();

    public void login(AppUser appUser){
        appUser = appUserService.findById(appUser.getId());
        this.logados.add(appUser);
    }

    public List<AppUser> usersLogged(){
        HashSet<AppUser> appUsers = Sets.newHashSet(this.logados);
        return Lists.newArrayList(appUsers);
    }

    public void logout(String userId){
        Iterator<AppUser> userIterator = logados.iterator();
        while (userIterator.hasNext()){
            AppUser appUser = userIterator.next();
            if(appUser.getId().equals(userId)){
                userIterator.remove();
                break;
            }
        }
    }

    public boolean isLogged(String userId){
        for (AppUser appUser : logados) {
            if (appUser.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

}
