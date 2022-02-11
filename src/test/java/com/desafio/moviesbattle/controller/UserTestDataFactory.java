package com.desafio.moviesbattle.controller;

import com.desafio.moviesbattle.model.CadastroDeUsuarioRequest;
import com.desafio.moviesbattle.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;

@Service
public class UserTestDataFactory {

    @Autowired
    private UsuarioService userService;

    public String criarUsuario(String username,
                               String password) throws ValidationException {
        CadastroDeUsuarioRequest cadastroDeUsuarioRequest = new CadastroDeUsuarioRequest(username, password);


        var usuarioCriado = userService.criar(cadastroDeUsuarioRequest);

        return usuarioCriado;
    }


}