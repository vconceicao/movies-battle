package com.desafio.moviesbattle;

import com.desafio.moviesbattle.model.CadastroDeUsuarioRequest;
import com.desafio.moviesbattle.services.UsuarioService;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final List<String> usernames = List.of(
            "jogador1",
            "jogador2"

    );

    private final String password = "Test12345_";

    private final UsuarioService usuarioService;

    public DatabaseInitializer(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        for (int i = 0; i < usernames.size(); ++i) {

            var cadastroDeUsuarioRequest = new CadastroDeUsuarioRequest(usernames.get(i), this.password);

            var criar = usuarioService.criar(cadastroDeUsuarioRequest);
        }
    }

}