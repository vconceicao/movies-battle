package com.desafio.moviesbattle.model;

import lombok.Getter;

@Getter
public class EncerrarPartidaRequest {

    private String nomeDoJogador;


    public EncerrarPartidaRequest(String nomeDoJogador) {
        this.nomeDoJogador = nomeDoJogador;
    }

    public EncerrarPartidaRequest() {
    }
}
