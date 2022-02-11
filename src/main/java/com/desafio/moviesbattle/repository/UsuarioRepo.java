package com.desafio.moviesbattle.repository;

import com.desafio.moviesbattle.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepo extends CrudRepository<Usuario, Long> {


    Optional<Usuario> findUserByUsername(String username);


}
