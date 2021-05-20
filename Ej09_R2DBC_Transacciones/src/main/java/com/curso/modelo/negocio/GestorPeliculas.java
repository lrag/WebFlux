package com.curso.modelo.negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.entidad.PeliculaHistorico;
import com.curso.modelo.persistencia.PeliculaHistoricoRepositorio;
import com.curso.modelo.persistencia.PeliculaRepositorio;
import com.curso.modelo.persistencia.PremioRepositorio;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class GestorPeliculas {

	@Autowired private PeliculaRepositorio peliculaRepo;
	@Autowired private PremioRepositorio premioRepo;
	@Autowired private PeliculaHistoricoRepositorio peliculaHistoricoRepo;

	//@Transactional(rollbackFor = Exception.class)
	public Mono<List<Pelicula>> pruebaTransacciones(){
		
		/*
		Pelicula[] peliculas = new Pelicula[10];
		int escort;
		for(escort=0; escort<10; escort++) {
			peliculas[a] = new Pelicula(null, "T"+escort, "D"+escort, "G"+escort, 1980+escort);
		}
		Flux
			.just(peliculas)
			.flatMap( pelicula -> {
				System.out.println("Insertando: "+pelicula);
				return peliculaRepo.save(pelicula);
			});
		*/
		
		return Flux
			.range(1, 10).map( n -> {
				return new Pelicula(null, "T"+n, "D"+n, "G"+n, n);
			})
			.flatMap( pelicula -> {

				if(pelicula.getYear()==9) {
					return Mono.error(new Exception("Muerte y destrucción"));
				}
				
				System.out.println("Insertando: "+pelicula);
				return peliculaRepo.save(pelicula);
			})
			.collect(Collectors.toList());		
	}	
	
	public Mono<Pelicula> insertar(Pelicula pelicula) {
		//Lógica de negocio 
		return peliculaRepo.save(pelicula);
	}
	
	public Mono<Pelicula> modificar(Pelicula pelicula) {
		//Lógica de negocio 
		return peliculaRepo.save(pelicula);
	}	
	
	public Mono<Void> borrar(Integer idPelicula) {
		System.out.println("========================================");
		System.out.println("Borrando la pelicula: "+idPelicula);
		
		PeliculaHistorico ph = new PeliculaHistorico();
		
		return peliculaRepo
			.findById(idPelicula) //Mono<Pelicula>
			.subscribeOn(Schedulers.boundedElastic())
			.flatMapMany( pelicula -> {
				ph.setTitulo(pelicula.getTitulo());
				ph.setDirector(pelicula.getDirector());
				ph.setGenero(pelicula.getGenero());
				ph.setYear(pelicula.getYear());
				return premioRepo.findByIdPelicula(idPelicula); //De aqui sale un flujo
			}) 
			.map( premio -> premio.getNombre())
			.collect(Collectors.joining(", ", "Premios:", "."))
			.flatMap( premios -> { //Aqui llega un mono
				ph.setPremios(premios);
				return peliculaHistoricoRepo.save(ph); //Mono<Peliculahistorico>
			})
			.flatMap( phInsertada -> {
				return peliculaRepo.deleteById(idPelicula);
			})
			//Este do on success no sirve para nada
			.doOnSuccess( x -> System.out.println("Pelicula borrada!!!!!!"));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	


		//Aqui tenemos tres consultas que ejecutar!!!
		/*
		Mono<Void> mono = peliculaRepo
			.findById(idPelicula) //devuelve Mono<Pelicula>
			.flatMap( pelicula -> {
				System.out.println("Pelicula encontrada:"+pelicula);
				PeliculaHistorico ph = new PeliculaHistorico(pelicula);
				return peliculaHistoricoRepo.save(ph); //devuelve Mono<PeliculaHistorico>
			})	
			.flatMapMany( phInsertada -> { //PAsamos de Mono a Flux
				System.out.println("PeliculaHistorico insertada:"+phInsertada);
				System.out.println("Buscando los premios");
				return premioRepo.findByIdPelicula(idPelicula); //devuelve Flux<Premio>
			})
			.collect(Collectors.toList())
			.flatMap( premios -> {			
				System.out.println("Premios:"+premios);				
				//Guardar los premios...				
				return peliculaRepo.deleteById(idPelicula); //Devuelve un Mono<Void>
			})
			.doOnSuccess( nada -> System.out.println("Pelicula borrada"));
		
		return mono;
	}	
	*/
}
