package com.desafio.moviesbattle.controller;

import com.desafio.moviesbattle.model.PalpiteRequest;
import com.desafio.moviesbattle.model.RodadaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static com.desafio.moviesbattle.controller.JsonHelper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("jogador1")
class RodadaControllerTest {


    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final PartidaTestDataFactory partidaTestDataFactory;

    @Autowired
    public RodadaControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, PartidaTestDataFactory partidaTestDataFactory) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.partidaTestDataFactory = partidaTestDataFactory;
    }

    @Test
    void iniciarRodada() throws Exception {


        var rodadaRequest = new RodadaRequest("island", "Avengers");

                 this.mockMvc
                .perform(post("/api/moviesbattle/iniciar-rodada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, rodadaRequest)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void fazerPalpite() throws Exception {


        var rodadaRequest = new RodadaRequest("Fight Club", "Avengers");

        this.mockMvc
                .perform(post("/api/moviesbattle/iniciar-rodada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, rodadaRequest)))
                .andExpect(status().isOk())
                .andReturn();

        var palpiteRequest = new PalpiteRequest(1, "jogador1");

        this.mockMvc
                .perform(post("/api/moviesbattle/palpite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, palpiteRequest)))
                .andExpect(status().isOk())
                .andReturn();

    }


    @Test
    void encerrarPartida() throws Exception {


        var rodadaRequest = new RodadaRequest("Fight Club", "Avengers");

        this.mockMvc
                .perform(post("/api/moviesbattle/iniciar-rodada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, rodadaRequest)))
                .andExpect(status().isOk())
                .andReturn();

        var palpiteRequest = new PalpiteRequest(1, "jogador1");

        this.mockMvc
                .perform(post("/api/moviesbattle/palpite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, palpiteRequest)))
                .andExpect(status().isOk())
                .andReturn();

        this.mockMvc
                .perform(get("/api/moviesbattle/fechar-partida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, palpiteRequest)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void rankingJogadores() throws Exception {


        partidaTestDataFactory.criarCenarios();
        this.mockMvc
                .perform(get("/api/moviesbattle/ranking-jogadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, "")))
                .andExpect(status().isOk())
                .andReturn();

    }


}