package com.desafio.moviesbattle.services;

import com.desafio.moviesbattle.model.CadastroDeUsuarioRequest;
import com.desafio.moviesbattle.model.Usuario;
import com.desafio.moviesbattle.repository.UsuarioRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.xml.bind.ValidationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class UsuarioServiceTest {

    @Test
    void obterUsuarioPorNome() {
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioRepo usuarioRepo = Mockito.mock(UsuarioRepo.class);
        Mockito.when(usuarioRepo.findUserByUsername(eq("test1"))).thenReturn(Optional.of(new Usuario("test1", "123")));


        var usuarioService = new UsuarioService(usuarioRepo, null);

        var userDetails = usuarioService.loadUserByUsername("test1");

        assertAll(() -> assertEquals("test1", userDetails.getUsername()));
    }


    @Test
    void obterUsuarioPorNomeNaoExistenteCadastrado() {
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioRepo usuarioRepo = Mockito.mock(UsuarioRepo.class);
        Mockito.when(usuarioRepo.findUserByUsername(eq("test1"))).thenReturn(Optional.empty());


        var usuarioService = new UsuarioService(usuarioRepo, null);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.loadUserByUsername("test1"));
    }


    @Test
    void criarUsuario() throws ValidationException {
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioRepo usuarioRepo = Mockito.mock(UsuarioRepo.class);
        Mockito.when(usuarioRepo.save(any())).thenReturn(new Usuario("test", "123"));
        Mockito.when(passwordEncoder.encode(any())).thenReturn(any());


        var usuarioService = new UsuarioService(usuarioRepo, passwordEncoder);

        usuarioService.criar(new CadastroDeUsuarioRequest("test", "test"));

        Mockito.verify(usuarioRepo, Mockito.times(1)).save(any());
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(any());

    }

    @Test
    void criarUsuarioJaExistente() throws ValidationException {
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UsuarioRepo usuarioRepo = Mockito.mock(UsuarioRepo.class);
        Mockito.when(usuarioRepo.findUserByUsername(eq("test1"))).thenReturn(Optional.of(new Usuario("test1", "123")));
        Mockito.when(passwordEncoder.encode(any())).thenReturn(any());


        var usuarioService = new UsuarioService(usuarioRepo, passwordEncoder);

        assertThrows(ValidationException.class, () ->usuarioService.criar(new CadastroDeUsuarioRequest("test1", "test")));



    }


}