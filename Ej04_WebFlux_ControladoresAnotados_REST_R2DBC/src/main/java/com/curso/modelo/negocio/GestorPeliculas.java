package com.curso.modelo.negocio;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.entidad.PeliculaHistorico;
import com.curso.modelo.persistencia.PeliculaHistoricoRepositorio;
import com.curso.modelo.persistencia.PeliculaRepositorio;
import com.curso.modelo.persistencia.PremioRepositorio;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class GestorPeliculas {

	@Autowired private PeliculaRepositorio peliculaRepo;
	@Autowired private PremioRepositorio premioRepo;
	@Autowired private PeliculaHistoricoRepositorio peliculaHistoricoRepo;

	public Mono<Pelicula> insertar(Pelicula pelicula) {
		//Lógica de negocio 
		return peliculaRepo.save(pelicula);
	}
	
	public Mono<Pelicula> modificar(Pelicula pelicula) {
		//Lógica de negocio 
		return peliculaRepo.save(pelicula);
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Mono<Void> borrar(Integer idPelicula) {
		//Buscar la pelicula y obtener sus datos
		//Guardar los datos en el histórico
		//Borrar la película		
		
		System.out.println("==========================================");
		System.out.println("GestorPeliculas.borrar");

		//Imperativo:
		//Pelicula p = peliculaRepo.findById(idPelicula);
		//PeliculaHistorico ph = new PeliculaHistorico(p);
		//peliculaHistoricoRepo.save(ph);
		//peliculaRepo.delete(p);		
		
		//Desastroso. encargamos al hilo esperar a las tres consultas:
		/*
		PeliculaHistorico ph = new PeliculaHistorico();
		
		peliculaRepo.findById(idPelicula).subscribe( p -> {
			if(p==null) {
				//
			}
			System.out.println("Pelicula encontrada:"+p);
			ph.setTitulo(p.getTitulo());
			ph.setDirector(p.getDirector());
			ph.setGenero(p.getGenero());
			ph.setYear(p.getYear());
		});
		
		peliculaHistoricoRepo.save(ph).subscribe( phBis -> {
			System.out.println("LLA SE A INSERTAO");
		});
		
		peliculaRepo.deleteById(idPelicula).doOnSuccess(v -> System.out.println("LLA SE A VORRADO, JAJA")).subscribe();
		*/
		
		//Callbacks y callback hell
		//Al menos podemos utilizar subscribeOn
		/*
		System.out.println("CALLBACKS");
		peliculaRepo
			.findById(idPelicula)
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe( p -> {
				System.out.println("Pelicula encontrada:"+p);
				PeliculaHistorico ph = new PeliculaHistorico(p);
				peliculaHistoricoRepo
					.save(ph)
					.subscribeOn(Schedulers.boundedElastic())
					.subscribe( phInsertada -> {
						System.out.println("PeliculaHistorico insertada:"+phInsertada);
						peliculaRepo
							.delete(p)
							.subscribeOn(Schedulers.boundedElastic())
							.subscribe( v -> { 
								System.out.println("ESTO NO SALDRÁ");	
							});
					});				
			});	
		*/
		
		//Aqui tenemos tres consultas que ejecutar!!!
		
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
	
}
