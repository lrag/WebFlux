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
				ph.setId(null);
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
		
		//Imperativo:
		//Pelicula p = peliculaRepo.findById(idPelicula);
		//PeliculaHistorico ph = new PeliculaHistorico(p);
		//peliculaHistoricoRepo.save(ph);
		//peliculaRepo.delete(p);
		
		
		peliculaRepo.findById(idPelicula).subscribe( p -> {
			
			if(p==null) {
				//
			}
			
			System.out.println("Pelicula encontrada:"+p);
			
		});
		
		
		
		
		
		
		return null;
		
		
		//Lógica de negocio 
		//return peliculaRepo.deleteById(idPelicula);
	}	
	
}
