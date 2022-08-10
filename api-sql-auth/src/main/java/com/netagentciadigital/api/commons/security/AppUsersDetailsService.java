package com.netagentciadigital.api.commons.security;

import com.netagentciadigital.api.model.AppUser;
import com.netagentciadigital.api.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;


@Service
public class AppUsersDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(email);
        if (appUser == null) {
            throw new UsernameNotFoundException("Username invalid!");
        }

        return new User(appUser.getEmail(), appUser.getPassword(), getPermissions(appUser));
    }

    private Collection<? extends GrantedAuthority> getPermissions(AppUser appUser) {
        //Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        //for(String perfil: appUser.getCodigosPerfil()) {
        //    authorities.add(new SimpleGrantedAuthority(perfil));
        // }
        return new HashSet<>();
    }

}
