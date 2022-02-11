package com.desafio.moviesbattle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = "com.desafio.moviesbattle")
@EnableFeignClients
public class MoviesBattleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesBattleApplication.class, args);
    }

}
