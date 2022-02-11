package com.desafio.moviesbattle.services;

import com.desafio.moviesbattle.client.FilmesClient;
import com.desafio.moviesbattle.model.*;
import com.desafio.moviesbattle.repository.PartidaRepo;
import com.desafio.moviesbattle.repository.UsuarioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartidaService {


    private final FilmesClient filmesClient;

    private HashSet<Pair<String, String>> paresDeFilmes = new HashSet<>();

    private Rodada rodada;

    private int RODADAS_DO_JOGO = 3;
    private int numeroDeRodadas;
    private int erros;
    private int acertos;

    private final UsuarioRepo usuarioRepo;
    private final PartidaRepo partidaRepo;


    public Rodada getRodada(RodadaRequest rodadaRequest) {
        Assert.isNull(rodada, "O jogador deve realizar o palpite antes de terminar a rodada ");

        //validar os filmes repetidos
        Assert.isTrue(!rodadaRequest.getFilme1().equals(rodadaRequest.getFilme2()), "Os filmes nao pode ser iguais");

        FilmeResponse filme = consultarFilmeNoImdb(rodadaRequest.getFilme1());
        FilmeResponse filme2 = consultarFilmeNoImdb(rodadaRequest.getFilme2());

        Pair<String, String> pair = Pair.of(rodadaRequest.getFilme1(), rodadaRequest.getFilme2());
        Pair<String, String> reversedPair = Pair.of(rodadaRequest.getFilme2(), rodadaRequest.getFilme1());


        Assert.isTrue(this.paresDeFilmes.add(pair), "Este par de filmes ja foi cadastrado");
        Assert.isTrue(this.paresDeFilmes.add(reversedPair), "Este par de filmes ja foi cadastrado");


        Integer vencedor;
        if (filme.compareTo(filme2) < 0) {
            vencedor = 1;
        } else {
            vencedor = 2;
        }

        Rodada rodada = rodadaRequest.toModel(vencedor);

        this.rodada = rodada;
        numeroDeRodadas++;
        return rodada;
    }

    private FilmeResponse consultarFilmeNoImdb(String nomeDoFilme) {
        return filmesClient.getFilme("7a8839d8", nomeDoFilme);
    }

    public String fazerPalpite(PalpiteRequest palpiteRequest, String usuario) {
        Assert.isTrue(Objects.nonNull(rodada), "O jogador precisa iniciar uma rodada antes de fazer o palpite");

        String resultado = "";

        if (palpiteRequest.getPalpite().equals(rodada.getResultado())) {
            acertos++;
            resultado = "Acertou";
        }else{
            erros++;
            resultado = "Errou";
            if (erros >= 3) {

                return encerrar(usuario);
            }

        }

        if (numeroDeRodadas >= RODADAS_DO_JOGO) {

            return  encerrar(usuario);
        }

        this.rodada = null;
        return resultado;
    }

    public String encerrar(String nomeUsuario) {

        var usuario = usuarioRepo.findUserByUsername(nomeUsuario).orElseThrow(() -> new IllegalArgumentException("Usuario nao existe"));

        int pontuacao = calcularPontuacao(numeroDeRodadas, acertos);

        var partida = new Partida(usuario, numeroDeRodadas, acertos, erros, pontuacao);

        System.out.println("Partida");
        partidaRepo.save(partida);
        System.out.println("Partida salva com sucesso");

        var sumario = "Fim de jogo.\nNome: " + nomeUsuario + "Tentativas e Acertos: " + numeroDeRodadas + "/ " + acertos;


        limparDadosDaPartida();
        return partida.toString();
    }

    private void limparDadosDaPartida() {
        this.paresDeFilmes.clear();
        this.rodada = null;
        this.acertos = 0;
        this.erros = 0;
        this.numeroDeRodadas = 0;
    }

    private int calcularPontuacao(int numeroDeRodadas, int acertos) {
       return  ((acertos * 100)/numeroDeRodadas) * numeroDeRodadas;

    }


    public String  getRankingDeJogadores() {


        var partidasI = partidaRepo.findAll();

        Assert.notNull(partidasI, "A Lista ser pode ser nula");


        List<Partida> partidas = new ArrayList<>();

        partidasI.forEach(partida -> partidas.add(partida));

        Assert.notEmpty(partidas, "A Lista ser pode ser vazia");



        var collect = partidas.stream().sorted()
                .map(partida -> partida.getUsuario().getUsername() + " - " + partida.getPontuacao())
                .collect(Collectors.toList());


        return collect.toString();
    }



}
