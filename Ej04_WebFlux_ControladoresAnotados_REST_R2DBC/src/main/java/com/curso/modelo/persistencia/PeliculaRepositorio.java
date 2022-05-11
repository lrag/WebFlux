package com.curso.modelo.persistencia;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.curso.modelo.entidad.Pelicula;

import reactor.core.publisher.Mono;

@Repository
public interface PeliculaRepositorio extends ReactiveCrudRepository<Pelicula, Integer>{
	//Mono<Pelicula> save(Pelicula pelicula);
}

