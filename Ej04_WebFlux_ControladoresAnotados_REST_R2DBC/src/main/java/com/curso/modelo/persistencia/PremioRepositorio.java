package com.curso.modelo.persistencia;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.entidad.Premio;

import reactor.core.publisher.Flux;

@Repository
public interface PremioRepositorio extends ReactiveCrudRepository<Premio, Integer>{
		
	public Flux<Premio> findByIdPelicula(Integer idPelicula);
	
}

