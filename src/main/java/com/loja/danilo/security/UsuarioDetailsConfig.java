package com.loja.danilo.security;

import com.loja.danilo.models.Usuario;
import com.loja.danilo.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Transactional
@Repository
public class UsuarioDetailsConfig implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.usuario(login);
        if (usuario == null) throw new UsernameNotFoundException("Usuário não encontrado!");
        return new User(usuario.getLogin(),
                        usuario.getPassword(),
                        true,
                        true,
                        true,
                        true,
                        usuario.getAuthorities());
    }
}
