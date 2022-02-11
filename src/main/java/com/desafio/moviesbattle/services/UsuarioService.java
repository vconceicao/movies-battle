package com.desafio.moviesbattle.services;

import com.desafio.moviesbattle.model.CadastroDeUsuarioRequest;
import com.desafio.moviesbattle.model.Usuario;
import com.desafio.moviesbattle.repository.UsuarioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {



    private final UsuarioRepo usuarioRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String nomeUsuario) throws UsernameNotFoundException {

        return usuarioRepo.findUserByUsername(nomeUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado " + nomeUsuario));

    }

    @Transactional
    public String criar(CadastroDeUsuarioRequest cadastroDeUsuarioRequest) throws ValidationException {

        if (usuarioRepo.findUserByUsername(cadastroDeUsuarioRequest.getNome()).isPresent()) {
            throw new ValidationException("O usuario já existe");
        }

        Usuario usuario = cadastroDeUsuarioRequest.toModel(passwordEncoder);
        var usuarioCriado = usuarioRepo.save(usuario);

        return usuarioCriado.getUsername();
    }
}
