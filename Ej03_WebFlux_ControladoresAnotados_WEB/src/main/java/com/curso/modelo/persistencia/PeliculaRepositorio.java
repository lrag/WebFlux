package com.curso.modelo.persistencia;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.curso.modelo.entidad.Pelicula;

public interface PeliculaRepositorio extends ReactiveCrudRepository<Pelicula, Integer> {

}
