package com.desafio.moviesbattle.repository;

import com.desafio.moviesbattle.model.Partida;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaRepo extends CrudRepository<Partida, Long> {
}
