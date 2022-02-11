package com.desafio.moviesbattle.controller;

import com.desafio.moviesbattle.model.CadastroDeUsuarioRequest;
import com.desafio.moviesbattle.model.Partida;
import com.desafio.moviesbattle.model.Usuario;
import com.desafio.moviesbattle.repository.PartidaRepo;
import com.desafio.moviesbattle.repository.UsuarioRepo;
import com.desafio.moviesbattle.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.Optional;
import java.util.Random;

@Service
public class PartidaTestDataFactory {


    @Autowired
    private PartidaRepo partidaRepo;

    @Autowired
    private UserTestDataFactory userFactory;

    @Autowired
    private UsuarioRepo usuarioRepo;

    private Partida criarPartida(String nomeDoUsuario, int numeroDeRodadas, int acertos, int erros, int pontuacao ) throws ValidationException {


       var usuario = usuarioRepo.findUserByUsername(nomeDoUsuario).get();
       var partida = new Partida(usuario, numeroDeRodadas, acertos, erros, pontuacao);
       return partidaRepo.save(partida);

    }

    public void criarCenarios() throws ValidationException {
        var usuario1 = userFactory.criarUsuario("test"+ Math.random(), "123");
        var usuario2 = userFactory.criarUsuario("test"+ Math.random(), "456");
        var usuario3 = userFactory.criarUsuario("test"+ Math.random(), "789");

        criarPartida(usuario1, 3, 3, 0, 300);
        criarPartida(usuario2, 3, 2, 1, 200);
        criarPartida(usuario3, 3, 1, 2, 100);


    }



}