package com.curso.modelo.persistencia;

import org.springframework.stereotype.Repository;

import com.curso.modelo.entidad.Pelicula;

import reactor.core.publisher.Flux;

@Repository
public class PeliculaRepositorio {

	
	public Flux<Pelicula> findAll() {

		return Flux.just(new Pelicula(1,"T1","D1","G1",2000),
				         new Pelicula(1,"T2","D1","G1",2000),
				         new Pelicula(1,"T3","D1","G1",2000),
				         new Pelicula(1,"T4","D1","G1",2000),
				         new Pelicula(1,"T5","D1","G1",2000));
		
	}
	
	
	
}
