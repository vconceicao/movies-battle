package com.desafio.moviesbattle.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PalpiteRequest {

    private Integer palpite;
    private String nomeDoJogador;


    public PalpiteRequest(Integer palpite, String nomeDoJogador) {
        this.palpite = palpite;
        this.nomeDoJogador = nomeDoJogador;
    }
}
