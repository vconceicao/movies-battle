package com.desafio.moviesbattle.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Rodada {

    private final Integer resultado;
    private final  String filme1;
    private final String filme2;



    public Integer getResultado() {
        return resultado;
    }

    @Override
    public String toString() {
        return "Qual o filme com melhor pontuacao?\n " +
                "\nDigitar 1 para filme1 -> " + filme1 +
                "\nDigitar 2 para filme2 ->  " + filme2;

    }
}
