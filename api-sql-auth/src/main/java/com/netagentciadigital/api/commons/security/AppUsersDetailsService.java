package com.netagentciadigital.api.commons.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AppUsersDetailsService implements UserDetailsService {

    @Autowired
    private RepositorioDeUsuario repositorioDeUsuario;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repositorioDeUsuario.findByEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Username invalid!");
        }

        return new User(usuario.getEmail(), usuario.getSenha(), getPermissions(usuario));
    }

    private Collection<? extends GrantedAuthority> getPermissions(Usuario usuario) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for(String perfil: usuario.getCodigosPerfil()) {
            authorities.add(new SimpleGrantedAuthority(perfil));
        }
        return authorities;
    }

}
