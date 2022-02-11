package com.desafio.moviesbattle.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class CadastroDeUsuarioRequest {

    private String nome;
    private String password;

    public CadastroDeUsuarioRequest(String nome, String password) {
        this.nome = nome;
        this.password = password;
    }

    public Usuario toModel(PasswordEncoder passwordEncoder) {
        return new Usuario(nome, passwordEncoder.encode(password));
    }

}
