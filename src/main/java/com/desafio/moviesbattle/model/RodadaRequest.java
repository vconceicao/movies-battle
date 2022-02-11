package com.desafio.moviesbattle.model;

import lombok.Getter;

@Getter
public class RodadaRequest {

    private final String filme1;
    private final String filme2;

    public RodadaRequest(String filme1, String filme2) {
        this.filme1 = filme1;
        this.filme2 = filme2;
    }

    public Rodada toModel(Integer resultado) {

        return new Rodada(resultado, filme1, filme2) ;
    }
}
