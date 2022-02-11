package com.desafio.moviesbattle.controller;

import com.desafio.moviesbattle.model.AuthRequest;
import com.desafio.moviesbattle.model.CadastroDeUsuarioRequest;
import com.desafio.moviesbattle.model.Usuario;
import com.desafio.moviesbattle.security.JwtTokenUtil;
import com.desafio.moviesbattle.services.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;

@Tag(name = "Autenticacao")

@RestController
@RequestMapping(path = "api/public")
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UsuarioService usuarioService;


    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthRequest request) {


        try {
            var authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getPassword()));

            var principal = (Usuario) authenticate.getPrincipal();

            var token = jwtTokenUtil.generateAccessToken(principal);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(principal.getUsername() + " token - "+ token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @PostMapping("cadastro")
    public ResponseEntity<String> cadastro(@RequestBody CadastroDeUsuarioRequest cadastroDeUsuarioRequest) throws ValidationException {
        return ResponseEntity.ok(usuarioService.criar(cadastroDeUsuarioRequest).concat(" Criado"));
    }

}
