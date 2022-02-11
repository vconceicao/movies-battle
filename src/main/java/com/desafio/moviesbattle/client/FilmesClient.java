package com.desafio.moviesbattle.client;

import com.desafio.moviesbattle.model.FilmeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "filmesClient", url = "http://www.omdbapi.com/")
public interface FilmesClient {

    @GetMapping(produces = "application/json")
    public FilmeResponse getFilme(@RequestParam(name = "apiKey") String apiKey, @RequestParam(name = "t") String titulo );
}
