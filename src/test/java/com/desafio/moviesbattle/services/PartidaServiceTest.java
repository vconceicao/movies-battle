package com.desafio.moviesbattle.services;

import com.desafio.moviesbattle.client.FilmesClient;
import com.desafio.moviesbattle.model.*;
import com.desafio.moviesbattle.repository.PartidaRepo;
import com.desafio.moviesbattle.repository.UsuarioRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.util.Pair;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


class PartidaServiceTest {


    @Test
    void iniciarRodadaEnviando2FilmesExistentes() {


        var filmesClient = Mockito.mock(FilmesClient.class);

        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("O Poderoso Chefao"))).thenReturn(new FilmeResponse("O Poderoso Chefao", "9.0"));
        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Clube da Luta"))).thenReturn(new FilmeResponse("Clube da Luta", "8.5"));

        var partidaService = new PartidaService(filmesClient, null, null);
        RodadaRequest rodadaRequest = new RodadaRequest("O Poderoso Chefao", "Clube da Luta");
        var rodada = partidaService.getRodada(rodadaRequest);

        assertAll(
                () -> assertEquals(1, rodada.getResultado()),
                () -> assertEquals("O Poderoso Chefao", rodada.getFilme1()),
                () -> assertEquals("Clube da Luta", rodada.getFilme2())
        );


    }

    @Test
    void iniciarRodadaEnviando2FilmesExistentesComSegundoFilmeComMaiorPontuacao() {


        var filmesClient = Mockito.mock(FilmesClient.class);

        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Resgate do Soldado Ryan"))).thenReturn(new FilmeResponse("Resgate do Soldado Ryan", "8.0"));
        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Clube da Luta"))).thenReturn(new FilmeResponse("Clube da Luta", "8.5"));

        var partidaService = new PartidaService(filmesClient, null, null);
        RodadaRequest rodadaRequest = new RodadaRequest("Resgate do Soldado Ryan", "Clube da Luta");
        var rodada = partidaService.getRodada(rodadaRequest);

        assertAll(
                () -> assertEquals(2, rodada.getResultado()),
                () -> assertEquals("Resgate do Soldado Ryan", rodada.getFilme1()),
                () -> assertEquals("Clube da Luta", rodada.getFilme2())
        );


    }


    @Test
    void iniciarRodadaEnviandoFilmesIguais() {


        RodadaRequest rodadaRequest = new RodadaRequest("Clube da Luta", "Clube da Luta");

        var partidaService = new PartidaService(null, null, null);

        var illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> partidaService.getRodada(rodadaRequest));


        assertEquals("Os filmes nao pode ser iguais", illegalArgumentException.getMessage());

    }

    @Test
    void iniciarRodadaSemFazerPalpiteAntes() {


        var filmesClient = Mockito.mock(FilmesClient.class);

        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Resgate do Soldado Ryan"))).thenReturn(new FilmeResponse("Resgate do Soldado Ryan", "8.0"));
        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Clube da Luta"))).thenReturn(new FilmeResponse("Clube da Luta", "8.5"));


        var partidaService = new PartidaService(filmesClient, null, null);

        ReflectionTestUtils.setField(partidaService, "rodada", new Rodada(1, "test1", "test2"));

        RodadaRequest rodadaRequest = new RodadaRequest("Resgate do Soldado Ryan", "Clube da Luta");


        var illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> partidaService.getRodada(rodadaRequest));


        assertEquals("O jogador deve realizar o palpite antes de terminar a rodada ", illegalArgumentException.getMessage());

    }


    @Test
    void iniciarRodadaEnviandoFilmesRepetidos() {


        var filmesClient = Mockito.mock(FilmesClient.class);

        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Resgate do Soldado Ryan"))).thenReturn(new FilmeResponse("Resgate do Soldado Ryan", "8.0"));
        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Clube da Luta"))).thenReturn(new FilmeResponse("Clube da Luta", "8.5"));


        var partidaService = new PartidaService(filmesClient, null, null);

        HashSet<Pair<String, String>> paresDeFilmes = new HashSet<>();

        paresDeFilmes.add(Pair.of("Resgate do Soldado Ryan", "Clube da Luta"));

        ReflectionTestUtils.setField(partidaService, "paresDeFilmes", paresDeFilmes);

        RodadaRequest rodadaRequest = new RodadaRequest("Resgate do Soldado Ryan", "Clube da Luta");

        var illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> partidaService.getRodada(rodadaRequest));


        assertEquals("Este par de filmes ja foi cadastrado", illegalArgumentException.getMessage());

    }

    @Test
    void iniciarRodadaEnviandoFilmesRepetidosComOrdemInvertida() {


        var filmesClient = Mockito.mock(FilmesClient.class);

        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Resgate do Soldado Ryan"))).thenReturn(new FilmeResponse("Resgate do Soldado Ryan", "8.0"));
        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Clube da Luta"))).thenReturn(new FilmeResponse("Clube da Luta", "8.5"));


        var partidaService = new PartidaService(filmesClient, null, null);

        HashSet<Pair<String, String>> paresDeFilmes = new HashSet<>();

        paresDeFilmes.add(Pair.of("Resgate do Soldado Ryan", "Clube da Luta"));
        paresDeFilmes.add(Pair.of("Clube da Luta", "Resgate do Soldado Ryan"));

        ReflectionTestUtils.setField(partidaService, "paresDeFilmes", paresDeFilmes);

        RodadaRequest rodadaRequest = new RodadaRequest("Clube da Luta", "Resgate do Soldado Ryan");

        var illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> partidaService.getRodada(rodadaRequest));


        assertEquals("Este par de filmes ja foi cadastrado", illegalArgumentException.getMessage());

    }


    @Test
    void fazerPalpiteAcertandoOFilme() {

        var filmesClient = Mockito.mock(FilmesClient.class);

        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Resgate do Soldado Ryan"))).thenReturn(new FilmeResponse("Resgate do Soldado Ryan", "8.0"));
        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Clube da Luta"))).thenReturn(new FilmeResponse("Clube da Luta", "8.5"));

        var partidaService = new PartidaService(filmesClient, null, null);
        RodadaRequest rodadaRequest = new RodadaRequest("Resgate do Soldado Ryan", "Clube da Luta");
        partidaService.getRodada(rodadaRequest);
        PalpiteRequest palpiteRequest = new PalpiteRequest(2, "test");
        var resultado = partidaService.fazerPalpite(palpiteRequest, "usuarioTeste");

        assertEquals("Acertou", resultado);


    }

    @Test
    void fazerPalpiteErrandoOFilme() {

        var filmesClient = Mockito.mock(FilmesClient.class);

        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Resgate do Soldado Ryan"))).thenReturn(new FilmeResponse("Resgate do Soldado Ryan", "8.0"));
        Mockito.when(filmesClient.getFilme(any(), Mockito.eq("Clube da Luta"))).thenReturn(new FilmeResponse("Clube da Luta", "8.5"));

        var partidaService = new PartidaService(filmesClient, null, null);
        RodadaRequest rodadaRequest = new RodadaRequest("Resgate do Soldado Ryan", "Clube da Luta");
        partidaService.getRodada(rodadaRequest);
        PalpiteRequest palpiteRequest = new PalpiteRequest(1, "teste");
        var resultado = partidaService.fazerPalpite(palpiteRequest, "usuarioTeste");

        assertEquals("Errou", resultado);

    }

    @Test
    void fazerPalpiteAntesDeIniciarUmaRodada() {


        var partidaService = new PartidaService(null, null, null);


        PalpiteRequest palpiteRequest = new PalpiteRequest(1, "test");

        var illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> partidaService.fazerPalpite(palpiteRequest, "usuarioTeste"));


        assertEquals("O jogador precisa iniciar uma rodada antes de fazer o palpite", illegalArgumentException.getMessage());


    }


    @Test
    void encerrarPartidaVoluntariamente() {

        var usuarioRepo = Mockito.mock(UsuarioRepo.class);
        var partidaRepo = Mockito.mock(PartidaRepo.class);

        Mockito.when(usuarioRepo.findUserByUsername(any())).thenReturn(Optional.of(new Usuario("test", "123")));
        Mockito.when(partidaRepo.save(any())).thenReturn(any(Partida.class));



        var partidaService = new PartidaService(null, usuarioRepo, partidaRepo);

        ReflectionTestUtils.setField(partidaService, "numeroDeRodadas", 3);
        ReflectionTestUtils.setField(partidaService, "acertos", 3);
        ReflectionTestUtils.setField(partidaService, "erros", 0);

        var resultado = partidaService.encerrar("test");

        var resultadoEsperado = "Resumo da Partida{" +
                "\n  usuario= " + "test" +
                "\n numeroDeRodadas=" + 3 +
                "\n acertos=" + 3 +
                "\n erros=" + 0 +
                "\n pontuacao=" + 300 +
                "\n }";

        assertEquals(resultadoEsperado, resultado);


    }


    @Test
    void encerrarPartidaAoErrarPor3VezesOPalpite() {

        var usuarioRepo = Mockito.mock(UsuarioRepo.class);
        var partidaRepo = Mockito.mock(PartidaRepo.class);

        Mockito.when(usuarioRepo.findUserByUsername(any())).thenReturn(Optional.of(new Usuario("test", "123")));
        Mockito.when(partidaRepo.save(any())).thenReturn(any(Partida.class));



        var partidaService = new PartidaService(null, usuarioRepo, partidaRepo);

        ReflectionTestUtils.setField(partidaService, "numeroDeRodadas", 3);
        ReflectionTestUtils.setField(partidaService, "acertos", 0);
        ReflectionTestUtils.setField(partidaService, "erros", 2);
        ReflectionTestUtils.setField(partidaService, "rodada", new Rodada(1, "teste1", "teste2"));


        partidaService.fazerPalpite(new PalpiteRequest(2, "teste"), "teste");

        Mockito.verify(usuarioRepo, Mockito.times(1)).findUserByUsername(any());
        Mockito.verify(partidaRepo, Mockito.times(1)).save(any());


    }

    @Test
    void encerrarPartidaAoAtingirOLimiteDeRodados() {

        var usuarioRepo = Mockito.mock(UsuarioRepo.class);
        var partidaRepo = Mockito.mock(PartidaRepo.class);

        Mockito.when(usuarioRepo.findUserByUsername(any())).thenReturn(Optional.of(new Usuario("test", "123")));
        Mockito.when(partidaRepo.save(any())).thenReturn(any(Partida.class));



        var partidaService = new PartidaService(null, usuarioRepo, partidaRepo);

        ReflectionTestUtils.setField(partidaService, "numeroDeRodadas", 3);
        ReflectionTestUtils.setField(partidaService, "acertos", 2);
        ReflectionTestUtils.setField(partidaService, "erros", 0);
        ReflectionTestUtils.setField(partidaService, "rodada", new Rodada(1, "teste1", "teste2"));



        partidaService.fazerPalpite(new PalpiteRequest(2, "teste"), "teste");

        Mockito.verify(usuarioRepo, Mockito.times(1)).findUserByUsername(any());
        Mockito.verify(partidaRepo, Mockito.times(1)).save(any());


    }


    @Test
    void  obterRankingDeJogadores() {



        var partidaRepo = Mockito.mock(PartidaRepo.class);
        var partidaService = new PartidaService(null, null, partidaRepo);

        Usuario usuario1 = new Usuario("teste1", "123");
        Usuario usuario2 = new Usuario("teste2", "123");
        Usuario usuario3 = new Usuario("teste3", "123");
        var partida1 = new Partida(usuario1, 3, 3, 0, 300);
        var partida2 = new Partida(usuario2, 3, 2, 1, 200);
        var partida3 = new Partida(usuario3, 3, 1, 2, 100);

        var partidas = List.of(partida1, partida2, partida3);

        Mockito.when(partidaRepo.findAll()).thenReturn(partidas);

        var rankingDeJogadores = partidaService.getRankingDeJogadores();

        assertEquals("[teste1 - 300, teste2 - 200, teste3 - 100]", rankingDeJogadores);
    }

    @Test
    void obterRankingDeJogadoresComListaVazia() {



        var partidaRepo = Mockito.mock(PartidaRepo.class);
        var partidaService = new PartidaService(null, null, partidaRepo);

        Iterable<Partida> partidas  = Collections.EMPTY_LIST;

        Mockito.when(partidaRepo.findAll()).thenReturn(partidas);


        assertThrows(IllegalArgumentException.class, () -> partidaService.getRankingDeJogadores());


    }


    @Test
    void obterRankingDeJogadoresComListaNula() {



        var partidaRepo = Mockito.mock(PartidaRepo.class);
        var partidaService = new PartidaService(null, null, partidaRepo);


        Mockito.when(partidaRepo.findAll()).thenReturn(null);


        assertThrows(IllegalArgumentException.class, () -> partidaService.getRankingDeJogadores());


    }










}