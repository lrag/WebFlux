package com.curso.modelo.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.entidad.PeliculaHistorico;
import com.curso.modelo.persistencia.PeliculaHistoricoRepositorio;
import com.curso.modelo.persistencia.PeliculaRepositorio;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class GestorPeliculas {

	@Autowired private PeliculaRepositorio peliculaRepo;
	@Autowired private PeliculaHistoricoRepositorio peliculaHistoricoRepo;
	
	public Mono<Pelicula> insertar(Pelicula pelicula) {

		System.out.println("GestorPeliculas.insertar:"+Thread.currentThread().getName());

		//Esto está bien
		//if(pelicula.getYear()<0) {
		//	return Mono.error(new Exception("Año negativo!"));
		//}
		//return peliculaRepo.save(pelicula);
		
		
		/*
		return Mono
			.just(pelicula)
			.flatMap( p -> {				
				if(p.getYear()<0)
					return Mono.error(new Exception("EL AÑO NO PUEDE SER NEGATIVO!"));
				else				
					return peliculaRepo.save(pelicula).subscribeOn(Schedulers.boundedElastic());
			});	
		*/		
		
		return Mono
			.just(pelicula)
			.filter(p -> p.getYear()>0 )
			.flatMap( p -> {				
				return peliculaRepo.save(pelicula).subscribeOn(Schedulers.boundedElastic());
			})
			.switchIfEmpty(Mono.error(new Exception("EL AÑO NO PUEDE SER NEGATIVO!")));
	}
	
	public Mono<String> borrar(Pelicula pelicula){		
		Mono<String> mono = peliculaRepo
			.findById(pelicula.getId())
			.flatMap( p -> {
				System.out.println("Encontrado:"+p);
				System.out.println("GestorPeliculas.borrar:"+Thread.currentThread().getName());
				PeliculaHistorico ph = new PeliculaHistorico(p);
				return peliculaHistoricoRepo.save(ph).subscribeOn(Schedulers.boundedElastic());
			}).flatMap( rs -> {
				System.out.println("Insertado:"+rs);
				System.out.println("GestorPeliculas.borrar:"+Thread.currentThread().getName());
				return peliculaRepo.delete(pelicula).subscribeOn(Schedulers.boundedElastic());
			}).then(Mono.just("OK"));
		
		return mono;						
	}
	
}
