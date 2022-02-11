package com.desafio.moviesbattle.controller;

import com.desafio.moviesbattle.model.AuthRequest;
import com.desafio.moviesbattle.model.CadastroDeUsuarioRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.xml.bind.ValidationException;

import static com.desafio.moviesbattle.controller.JsonHelper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

private  final MockMvc mockMvc;
private  final UserTestDataFactory userTestDataFactory;
private final ObjectMapper objectMapper;

@Autowired
    public AuthControllerTest(MockMvc mockMvc, UserTestDataFactory userTestDataFactory, ObjectMapper objectMapper) {

        this.mockMvc = mockMvc;
        this.userTestDataFactory = userTestDataFactory;
    this.objectMapper = objectMapper;
}

    @Test
    void loginComSucesso() throws Exception {
        var usuario = userTestDataFactory.criarUsuario("test", "test");

        var authRequest = new AuthRequest("test", "test");

        this.mockMvc
                .perform(post("/api/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, authRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

    }

    @Test
    void loginComFalha() throws Exception {
        var usuario = userTestDataFactory.criarUsuario("test2", "test");

        var authRequest = new AuthRequest("test", "test1");

        this.mockMvc
                .perform(post("/api/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, authRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andReturn();

    }

    @Test
    void criaUsuario() throws Exception {

        var cadastroDeUsuarioRequest = new CadastroDeUsuarioRequest("test3", "123");

        this.mockMvc
                .perform(post("/api/public/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(objectMapper, cadastroDeUsuarioRequest)))
                .andExpect(status().isOk())
                .andReturn();


    }




}