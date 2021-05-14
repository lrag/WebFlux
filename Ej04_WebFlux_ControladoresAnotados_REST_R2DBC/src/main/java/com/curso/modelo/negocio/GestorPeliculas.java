package com.curso.modelo.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.entidad.PeliculaHistorico;
import com.curso.modelo.persistencia.PeliculaHistoricoRepositorio;
import com.curso.modelo.persistencia.PeliculaRepositorio;

import reactor.core.publisher.Mono;

@Service
public class GestorPeliculas {

	@Autowired private PeliculaRepositorio peliculaRepo;
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
		/*
		Mono<String> mono = peliculaRepo
			.findById(pelicula.getId())
			.flatMap( p -> {
				System.out.println("Encontrado:"+p);
				System.out.println("GestorPeliculas.borrar:"+Thread.currentThread().getName());
				PeliculaHistorico ph = new PeliculaHistorico(p);
				return peliculaHistoricoRepo.save(ph);
			}).flatMap( rs -> {
				System.out.println("Insertado:"+rs);
				System.out.println("GestorPeliculas.borrar:"+Thread.currentThread().getName());
				return peliculaRepo.delete(pelicula);
			}).then(Mono.just("OK"));
		
		return mono;				 
		*/
		
		
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
		/*
		peliculaRepo
			.findById(idPelicula)
			.subscribe( p -> {
				PeliculaHistorico ph = new PeliculaHistorico(p);
				peliculaHistoricoRepo
					.save(ph)
					.subscribe(phBis -> {
						peliculaRepo.delete(p).subscribe( v -> {
								//
							});
					});				
			});
		*/
		
		peliculaRepo
			.findById(idPelicula)
			.map( p -> {
				System.out.println("Pelicula encontrada:"+p);
				PeliculaHistorico ph = new PeliculaHistorico(p);
				return ph;
			})
			.map(ph -> {
				System.out.println(ph);
				return ph;
			}).subscribe(x -> System.out.println("ASDF"));
		
		
		
		
		return null;
				
		//Lógica de negocio 
		//return peliculaRepo.deleteById(idPelicula);
	}	
	
}
