package com.desafio.moviesbattle.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

@Entity
@Getter
public class Partida implements Serializable, Comparable<Partida> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Usuario usuario;
    private int numeroDeRodadas;
    private int acertos;
    private int erros;
    private int pontuacao;


    public Partida() {
    }

    public Partida(Usuario usuario, int numeroDeRodadas, int acertos, int erros, int pontuacao) {
        this.usuario = usuario;
        this.numeroDeRodadas = numeroDeRodadas;
        this.acertos = acertos;
        this.erros = erros;
        this.pontuacao = pontuacao;
    }

    @Override
    public String toString() {
        return "Resumo da Partida{" +
                "\n  usuario= " + usuario.getUsername() +
                "\n numeroDeRodadas=" + numeroDeRodadas +
                "\n acertos=" + acertos +
                "\n erros=" + erros +
                "\n pontuacao=" + pontuacao +
                "\n }";
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partida partida = (Partida) o;
        return Objects.equals(id, partida.id) && Objects.equals(usuario, partida.usuario);
    }


    @Override
    public int hashCode() {
        return Objects.hash(usuario, pontuacao);
    }

    @Override
    public int compareTo(Partida o) {
        return Integer.compare( o.getPontuacao(),this.getPontuacao());
    }
}
