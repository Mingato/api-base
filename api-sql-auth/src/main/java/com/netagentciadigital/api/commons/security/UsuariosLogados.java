package com.netagentciadigital.api.commons.security;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UsuariosLogados {

    @Autowired
    private ServicoUsuario servicoUsuario;

    private final List<Usuario> logados = Lists.newArrayList();

    public void login(Usuario usuario){
        usuario = servicoUsuario.buscarPorId(usuario.getId());
        this.logados.add(usuario);
    }

    public List<Usuario> logados(){
        HashSet<Usuario> usuarios = Sets.newHashSet(this.logados);
        return Lists.newArrayList(usuarios);
    }

    public void logout(String usuarioLogoutId){
        Iterator<Usuario> usuarioIterator = logados.iterator();
        while (usuarioIterator.hasNext()){
            Usuario usuario = usuarioIterator.next();
            if(usuario.getId().equals(usuarioLogoutId)){
                usuarioIterator.remove();
                break;
            }
        }
    }

    public boolean estaLogado(String idUsuario){
        for (Usuario usuario : logados) {
            if (usuario.getId().equals(idUsuario)) {
                return true;
            }
        }
        return false;
    }

    public Usuario seleciona(String idUsuario, List<String> codigosPerfil){
        for (Usuario usuario : servicoUsuario.buscaTodos(codigosPerfil)) {
            if(usuario.getId().equals(idUsuario)){
                return usuario;
            }
        }
        throw new RuntimeException("Usuario nao logado");
    }

}
