package com.desafio.moviesbattle.controller;

import com.desafio.moviesbattle.model.EncerrarPartidaRequest;
import com.desafio.moviesbattle.security.JwtTokenUtil;
import com.desafio.moviesbattle.model.PalpiteRequest;
import com.desafio.moviesbattle.model.Rodada;
import com.desafio.moviesbattle.model.RodadaRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import com.desafio.moviesbattle.services.*;


@RestController
@Tag(name = "Rodada")
@RequestMapping(path = "api/moviesbattle")
public class RodadaController {

    private static final String AUTH_HEADER = "Authorization";


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PartidaService partidaService;

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/iniciar-rodada")
    public String iniciarRodada(@RequestBody RodadaRequest rodadaRequest) {


        Rodada rodada = partidaService.getRodada(rodadaRequest);
        return rodada.toString();
    }

    @PostMapping("/palpite")
    public String iniciarRodada(@RequestBody PalpiteRequest palpiteRequest){
        String usuario = getUser(palpiteRequest.getNomeDoJogador());
        return partidaService.fazerPalpite(palpiteRequest, usuario);
    }

    private String getUser(String nomeDoJogador) {

        var userDetails = usuarioService.loadUserByUsername(nomeDoJogador);
        return userDetails.getUsername();
    }

    @GetMapping("/fechar-partida")
    public String fecharPartida(@RequestBody EncerrarPartidaRequest encerrarPartidaRequest) {
        String usuario = getUser(encerrarPartidaRequest.getNomeDoJogador());

        return partidaService.encerrar(usuario);
    }



    @GetMapping("/ranking-jogadores")
    public String obterRankingDeJogadores(){

        return  partidaService.getRankingDeJogadores();
    }


    private String getUserByToken(HttpServletRequest request) {
        String authToken = request.getHeader(AUTH_HEADER);
        final String token = authToken.substring(7);
        return jwtTokenUtil.getUsername(token);
    }




}
