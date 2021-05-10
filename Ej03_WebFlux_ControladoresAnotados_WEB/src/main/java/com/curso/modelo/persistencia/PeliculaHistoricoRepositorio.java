package com.curso.modelo.persistencia;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.entidad.PeliculaHistorico;

public interface PeliculaHistoricoRepositorio extends ReactiveCrudRepository<PeliculaHistorico, Integer> {

}
