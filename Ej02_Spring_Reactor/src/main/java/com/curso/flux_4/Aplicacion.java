package com.curso.flux_4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class Aplicacion implements CommandLineRunner{

	@Autowired
	private Flujos flujos;
	
	@Autowired
	private PeliculaRepository peliculaRepo;

	@Autowired
	private PremioRepository premioRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(Aplicacion.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		/*
		////////////
		// CONCAT //
		////////////

		System.out.println("======================================");
		Flux.concat(
				//Primero se subscriben a este flujo y lo consumen entero
				flujos.numerosPares().subscribeOn(Schedulers.boundedElastic()),
				//Luego se subscriben a este
				flujos.numerosImpares().subscribeOn(Schedulers.boundedElastic())
			)
			.subscribe( n -> System.out.println(n) );
		
		Thread.sleep(10_000);

		
		Flux.concat(
				peliculaRepo.findById(1), 
				peliculaRepo.findById(2), 
				peliculaRepo.findById(3)
			)
			.subscribe(p -> System.out.println(p));
			
		peliculaRepo
			.findAllById(Arrays.asList(1,2,3))
			.subscribe( p -> System.out.println( "peliculaRepo.findAllById:"+p));
		
		
		///////////
		// MERGE //
		///////////
		
		System.out.println("======================================");
		Flux.merge(
				flujos.numerosPares().subscribeOn(Schedulers.boundedElastic()), 
				flujos.numerosImpares().subscribeOn(Schedulers.boundedElastic())
			)
			.subscribe( n -> System.out.println(n));
		
		Thread.sleep(10_000);
		
		
		////////////
		// FILTER //
		////////////

		System.out.println("======================================");		
		peliculaRepo
			.findAll()
			.filter(p -> p.getGenero().equals("Ci-fi"))
			.subscribe(p -> System.out.println(p));	
		
		
		/////////
		// MAP //
		/////////
		
		System.out.println("======================================");
		flujos
			.flujoPalabras()
			.subscribe( palabra -> System.out.println(palabra));
		
		System.out.println("======================================");
		flujos
			.flujoPalabras()
			.map( palabra -> palabra.toUpperCase())
			.subscribe( palabra -> System.out.println(palabra));


		System.out.println("======================================");
		flujos
			.flujoPalabras()
			.map( palabra -> palabra.toUpperCase() )
			.map( palabra -> palabra.length()+"-"+palabra)
			.subscribe( palabra -> System.out.println(palabra));
		

		System.out.println("======================================");
		//Lo mismo pero sin 'map'
		flujos
			.flujoPalabras()
			.subscribe( palabra -> System.out.println(palabra.length()+":"+palabra.toUpperCase()));		


		//////////////
		// FLAT MAP //
		//////////////
		
		System.out.println("======================================");
		//leer un fichero imagen
		//transformar el contendio en otro formato
		//crear un nuevo fichero
		*/
		
		
		//CALLBACK HELL
		/*
		flujos
			.leerFichero("movida.bmp")
			.subscribe( contenido -> {
				System.out.println("contenido:"+contenido);
				flujos
					.convertirImagen(contenido)
					.subscribe( nuevoContenido -> {
						System.out.println("nuevoContenido:"+nuevoContenido);
						flujos.escribirFichero("movida.jpg", nuevoContenido)
							.subscribe();
					});
			});
		
		flujos.leerFichero("imagen.bmp") //De aquí sale un simpático mono vestido de futbolista que dice 'we are the chimps!'
			.flatMap( contenido -> {
				System.out.println("Contenido del fichero:"+contenido);
				return flujos.convertirImagen(contenido); //Devuelve un mono
			})
			.flatMap( imagenConvertida -> {
				System.out.println("Imagen convertida:"+imagenConvertida);
				return flujos.escribirFichero(imagenConvertida, "imagen.jpg");
			})			
			.doOnSuccess( x -> {
				System.out.println("FIN:"+x);
			})
			.subscribe();	
		 */
		
		
		///////////////////
		// FLAT MAP MANY //
		///////////////////			
		
		System.out.println("======================================");		
		peliculaRepo
			.findById(3) //De aqui sale un Mono
			.flatMapMany( p -> {
				return premioRepo.findAllByIdPelicula(p.getId()); //De aqui sale un FLUX!
			})
			.subscribe(premio -> System.out.println(premio));			
				
		
		/////////////
		// COLLECT //
		/////////////
		
		System.out.println("======================================");		
		flujos
			.flujoPalabras()
			.collect(Collectors.toList())
			.subscribe(lista -> System.out.println(lista));
		
	}

}


