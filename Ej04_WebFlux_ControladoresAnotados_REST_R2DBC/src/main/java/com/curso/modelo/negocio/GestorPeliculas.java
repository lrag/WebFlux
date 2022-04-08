package com.curso.modelo.negocio;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		
		//Si no nos subscribimos a un Mono<Void> y proporcionamos un consumidor
		//este no se ejecutará porque no hay nada que consumir
		
		//Si lo hacemos así este método debería ser void o devolver un boolean
		//Y nos estamos cargando la reactividad
		//peliculaRepo.deleteById(idPelicula).subscribe(x -> System.out.println(x));
		
		//Si solo queremos borrar la película esta es la manera correcta: devolviendo el mono
		//return peliculaRepo.deleteById(idPelicula);
		
		//Imperativo, síncrono de toda la vida
		//Si peliculaRepo no fuera reactivo:
		//Pelicula p = peliculaRepo.findById(idPelicula);
		//PeliculaHistorico ph = new PeliculaHistorico(p);
		//peliculaRepo.save(ph);
		//peliculaRepo.deleteById(idPelicula);
		
		//
		//Si lo hacemos asi en nuestro local funciona pero nos estamos cargando todo lo reactivo que haya
		//		
		/*
		PeliculaHistorico ph = new PeliculaHistorico();
		peliculaRepo
			.findById(idPelicula)
			.subscribe( p-> {
				ph.setTitulo(p.getTitulo());
				ph.setDirector(p.getDirector());
				ph.setGenero(p.getGenero());
				ph.setYear(p.getYear());					
			});
	
		peliculaHistoricoRepo
			.save(ph)
			.subscribe( phInsertado -> System.out.println(phInsertado) );
		
		peliculaRepo
			.deleteById(idPelicula) 
			.subscribe(); //Es un Mono<Void>
		
		//Devolvemos un Mono<Void> para finjir que sabemos mogollón de programación reactiva
		return Mono.empty();
		*/		
		
		//
		//ESTO NO FUNCIONA
		//Se hacen las tres consultas en paralelo!
		//		
		/*
		peliculaRepo
			.findById(idPelicula)
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe( p-> {
				ph.setTitulo(p.getTitulo());
				ph.setDirector(p.getDirector());
				ph.setGenero(p.getGenero());
				ph.setYear(p.getYear());					
			});
	
		peliculaHistoricoRepo
			.save(ph)
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe( phInsertado -> System.out.println(phInsertado) );
		
		peliculaRepo
			.deleteById(idPelicula) 
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe(); //Es un Mono<Void>
		
		//Devolvemos un Mono<Void> para finjir que sabemos mogollón de programación reactiva
		return Mono.empty();		
		*/
		
		//
		//Con callbacks y callback hell, pero al menos podemos lograr que sea reactivo
		//
		System.out.println(Thread.currentThread().getName()+"-A borrar!");
		peliculaRepo
			.findById(idPelicula)
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe( p -> {
				System.out.println(Thread.currentThread().getName()+"-Película: "+p);
				PeliculaHistorico ph = new PeliculaHistorico(p);
				peliculaHistoricoRepo
					.save(ph)
					.subscribeOn(Schedulers.boundedElastic())
					.subscribe( phInsertada -> {
						System.out.println(Thread.currentThread().getName()+"-PH:"+phInsertada);						
						peliculaRepo
							.delete(p)
							.subscribeOn(Schedulers.boundedElastic())
							.subscribe( x ->{
								System.out.println(Thread.currentThread().getName()+"Pelicula borrada"); //Estó nunca se ejecutará
							});						
					});
			});
		
		return Mono.empty();	
		
		/*
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
				//String premios = listaPremios
				//	.stream()
				//	.map(premio -> premio.getNombre())
				//	.collect(Collectors.joining(", ", "Premios:", "."));
				ph.setPremios(premios);
				return peliculaHistoricoRepo.save(ph); //Mono<Peliculahistorico>
			})
			.flatMap( phInsertada -> {
				return peliculaRepo.deleteById(idPelicula);
			})
			//Este do on success no sirve para nada
			.doOnSuccess( x -> System.out.println("Pelicula borrada!!!!!!"));
		*/
		
					
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
