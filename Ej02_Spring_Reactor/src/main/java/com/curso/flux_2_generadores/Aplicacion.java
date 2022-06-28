package com.curso.flux_2_generadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.curso.modelo.entidad.Pelicula;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class Aplicacion implements CommandLineRunner{

	@Autowired
	private PeliculaRepositorio peliculaRepo;
	
	@Autowired
	private Flujos flujos;
	
	public static void main(String[] args) {
		SpringApplication.run(Aplicacion.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		/*
		System.out.println("=====================================");
		Flux<Long> flujoFinito = flujos.fluxNumerosAleatoriosFinito();
		flujoFinito.subscribe(numero -> {
			System.out.println(Thread.currentThread().getName()+"-Consumidor:"+numero);
		});
		*/
		
		//System.out.println("=====================================");
		//Flux<String> flujoEstado = flujos.flujoConEstado();
		//flujoEstado.subscribe(mensaje -> System.out.println(mensaje));
		 
		/*
		System.out.println("=====================================");
		Flux<String> flujoEstado2 = flujos.flujoConEstadoYStateConsumer();
		flujoEstado2.subscribe(mensaje -> System.out.println(mensaje));
				
		System.out.println("FIN");
		System.exit(0);
		*/		
			
		/*
		System.out.println("=============================================");
		List<Pelicula> peliculas = peliculaRepo.findAll();
		for(Pelicula p: peliculas) {
			System.out.println(p);
		}	
		
		Thread.sleep(1000);
		System.out.println("FIN");

		System.out.println("=============================================");
		peliculaRepo.findAll_Reactivo().subscribe( pelicula -> System.out.println(pelicula));	
		
		System.out.println("=============================================");
		peliculaRepo
			.findAll_Reactivo_Sin_Historias() //De aqui sale un Mono<List<Pelicula>>
			.subscribe(listado -> listado.forEach(p -> System.out.println(":"+p)));
		*/
			
		////////////////////////
		// CONTROL DE ERRORES //
		////////////////////////
		System.out.println("=============================================");
		peliculaRepo
			.findById(1000)
			.subscribe(
				pelicula -> System.out.println(pelicula), 
				error -> System.out.println(error.getMessage())
			);
		
		Thread.sleep(2000);
		
		System.out.println("=============================================");
		peliculaRepo
			.findById(1000)
			.doOnError(error -> System.out.println("1-"+error.getMessage())) //Cuidado que esto relanza la excepción
			.subscribe(
					pelicula -> System.out.println(pelicula), 
					error -> System.out.println("2-"+error.getMessage())
				);
		
		System.exit(0);				
		
		System.out.println("FIN del hilo main");
	}

}
