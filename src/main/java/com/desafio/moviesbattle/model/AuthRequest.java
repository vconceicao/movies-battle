package com.desafio.moviesbattle.model;

import lombok.Getter;

@Getter
public class AuthRequest {

   private String  usuario;
    private String password;

    public AuthRequest(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }
}
