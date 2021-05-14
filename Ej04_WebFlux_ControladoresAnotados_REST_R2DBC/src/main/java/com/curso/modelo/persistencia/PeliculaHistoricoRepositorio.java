package com.curso.modelo.persistencia;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.entidad.PeliculaHistorico;

import reactor.core.publisher.Flux;

@Repository
public interface PeliculaHistoricoRepositorio extends ReactiveCrudRepository<PeliculaHistorico, Integer>{
}

