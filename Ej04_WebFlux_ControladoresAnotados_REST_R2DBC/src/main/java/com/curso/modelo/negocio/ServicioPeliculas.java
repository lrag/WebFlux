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
public class ServicioPeliculas {

	@Autowired private PeliculaRepositorio peliculaRepo;
	@Autowired private PremioRepositorio premioRepo;
	@Autowired private PeliculaHistoricoRepositorio peliculaHistoricoRepo;

	public Mono<Pelicula> insertar(Pelicula pelicula) {

		//Aqui no pondremos código que esté fuera del stream que hemos de construir
		return peliculaRepo.save(pelicula).map(p -> {
			//Cualquier LN
			//va en eslabones de la cadena
			return p;
		});
				
		/*
		Si aqui nos subscribimos estamos forzando al hilo del event loop a ejecutar I/O bloqu
		y nos acabamos de cargar el rendimiento de toda la aplicación
		
		Mono<Pelicula> macaco = peliculaRepo.save(pelicula);
		Pelicula peliculaInsertada = new Pelicula(); 
		macaco.subscribe(pi -> {
			peliculaInsertada.setId(pi.getId());
			peliculaInsertada.setTitulo(pi.getTitulo());
			//..
		});
		return peliculaInsertada;

		//También podríamos haber hecho esto, que está igual de mal:
		return macaco.block();		
		 */
	}
	
	public Mono<Pelicula> modificar(Pelicula pelicula) {
		return peliculaRepo.save(pelicula);
	}	
	
	public Mono<Void> borrar(Integer idPelicula) {
		
		System.out.println("========================================");
		System.out.println("Borrando la pelicula: "+idPelicula);
		
		//Imperativo, síncrono de toda la vida
		//Si peliculaRepo no fuera reactivo:
		//Pelicula p = peliculaRepo.findById(idPelicula);
		//PeliculaHistorico ph = new PeliculaHistorico(p);
		//peliculaHistoricoRepo.save(ph);
		//peliculaRepo.deleteById(idPelicula);		
		
		//Si solo queremos borrar la película esta es la manera correcta: devolviendo el mono
		//return peliculaRepo.deleteById(idPelicula);

		
		//
		//Si lo hacemos asi en nuestro local funciona pero nos estamos cargando todo lo reactivo que haya
		//Estamos forzando al hilo del event loop a ejecutar las consultas!!!
		//	
		/*		
		Pelicula p = peliculaRepo
			.findById(idPelicula)
			.block();
		PeliculaHistorico ph = new PeliculaHistorico(p);
	
		peliculaHistoricoRepo
			.save(ph).block();
		
		peliculaRepo
			.deleteById(idPelicula).block();
		
		//Devolvemos un Mono<Void> para finjir que sabemos mogollón de programación reactiva
		return Mono.empty();
		*/		
		
		//
		//ESTO NO FUNCIONA
		//Se hacen las tres consultas en paralelo!
		//
		/*
		PeliculaHistorico ph = new PeliculaHistorico();
		peliculaRepo
			.findById(idPelicula)
			.subscribeOn(Schedulers.boundedElastic()) //No hace falta el subscribeOn porque los repositorios ya colocan 'publushOn'
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
		//Con callback hell el código es un infierno
		//Además, como nos subscribimos a los flujos/monos no podemos devolvelos
		//Y tampoco podemos devolver el resultado si el código está en un método
		//Y para más INRI no podremos avisar de que ha habido un fallo		
		/*
		System.out.println(Thread.currentThread().getName()+"-A borrar!");
		peliculaRepo
			.findById(idPelicula)
			.subscribe( p -> {
				System.out.println(Thread.currentThread().getName()+"-Película: "+p);
				PeliculaHistorico ph = new PeliculaHistorico(p);
				peliculaHistoricoRepo
					.save(ph)
					.subscribe( phInsertada -> {
						System.out.println(Thread.currentThread().getName()+"-PH:"+phInsertada);						
						peliculaRepo
							.delete(p)
							.subscribe( x ->{
								System.out.println(Thread.currentThread().getName()+"Pelicula borrada"); //Estó nunca se ejecutará
							});						
					});
			});
		
		return Mono.empty();
		*/

		
		//Esta cadena de operadores ya es correcta
		//-busca la película
		//-crea pelicula histórico y lo inserta
		//-borra la película

		/*
		return peliculaRepo
			.findById(idPelicula) //De aqui sale un Mono<Pelicula>
			.flatMap(pelicula -> {
				PeliculaHistorico ph = new PeliculaHistorico(pelicula);
				return peliculaHistoricoRepo.save(ph); //De aqui sale un Mono<Peliculahistorico>
			})
			.flatMap( peliculaHistoricoInsertada -> {
				return peliculaRepo.deleteById(idPelicula); //De aqui sale un Mono<Void>
			});
			*/

		return peliculaRepo
			.findById(idPelicula) //De aqui sale un Mono<Pelicula>
			.flatMap(pelicula -> {
				return premioRepo
					.findAllByIdPelicula(idPelicula)
					.map( premio -> premio.getNombre())
					.collect(Collectors.joining(", ","","."))
					.map( premios -> {
						PeliculaHistorico ph = new PeliculaHistorico(pelicula);
						ph.setPremios(premios);
						return ph;
					}); //De aqui sale un Mono<PeliculaHistorico>
			})
			.map( ph -> { 
				System.out.println("Pelicula historico:"+ph);
				return peliculaHistoricoRepo.save(ph); //De aqui sale un Mono<PeliculaHistorico>
			})
			.flatMap( peliculaHistoricoInsertada -> {
				System.out.println("Pelicula historico insertada");
				//return premioRepo.deleteByIdPelicula(idPelicula); //De aqui sale mono<Void> asi que el siguiente operador no se ejecuta!!!
				return premioRepo.deleteByIdPelicula(idPelicula).thenReturn(true); //De aqui sale un Mono<Boolean> y podemos seguir
			})
			.flatMap( bool ->{						
				System.out.println("Premios borrados");
				return peliculaRepo.deleteById(idPelicula); //De aqui sale un Mono<Void>
			});		
		
		
		/*
		return peliculaRepo
				.findById(idPelicula) //De aqui sale un Mono<Pelicula>
				.flatMap(pelicula -> {
					System.out.println("Pelicula:"+pelicula);
					PeliculaHistorico ph = new PeliculaHistorico(pelicula);
					return Mono.just(ph).zipWith(
						premioRepo
							.findAllByIdPelicula(idPelicula)    //de aqui van saliendo premios
							.map( premio -> premio.getNombre()) //de aqui salen los nombres de los premios
							.collect(Collectors.joining("",", ","."))); //De aqui sale un Mono<String> que tiene los nombres de los premios separados por comas 
				})
				.map(tupla -> { 
					PeliculaHistorico ph = tupla.getT1();
					String premios = tupla.getT2();
					ph.setPremios(premios);
					System.out.println("Pelicula historico:"+ph);
					return peliculaHistoricoRepo.save(ph); //De aqui sale un Mono<Peliculahistorico>
				})
				.flatMap( peliculaHistoricoInsertada -> {
					System.out.println("Pelicula historico insertada");
					//return premioRepo.deleteByIdPelicula(idPelicula); //De aqui sale mono<Void> asi que el siguiente operador no se ejecuta!!!
					return premioRepo.deleteByIdPelicula(idPelicula).thenReturn(true); //De aqui sale un Mono<Boolean> y podemos seguir
				})
				.flatMap( bool ->{						
					System.out.println("Premios borrados");
					return peliculaRepo.deleteById(idPelicula); //De aqui sale un Mono<Void>
				});
		*/
	}	
	
}
