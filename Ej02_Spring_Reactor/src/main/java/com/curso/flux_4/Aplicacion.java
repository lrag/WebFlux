package com.curso.flux_4;

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
	
	public static void main(String[] args) {
		SpringApplication.run(Aplicacion.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		////////////
		// CONCAT //
		////////////
		
		System.out.println("======================================");
		Flux.concat(flujos.numerosPares().subscribeOn(Schedulers.boundedElastic()), flujos.numerosImpares().subscribeOn(Schedulers.boundedElastic()))
			.subscribe( n -> System.out.println(n));
		
		Thread.sleep(10_000);

		Flux.concat(peliculaRepo.findById(1), peliculaRepo.findById(2), peliculaRepo.findById(3))
			.subscribe(p -> System.out.println(p));
		
		
		///////////
		// MERGE //
		///////////
		
		System.out.println("======================================");
		Flux.merge(flujos.numerosPares().subscribeOn(Schedulers.boundedElastic()), flujos.numerosImpares().subscribeOn(Schedulers.boundedElastic()))
			.subscribe( n -> System.out.println(n));
		
		Thread.sleep(10_000);
		
		
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
			.map( palabra -> palabra.toUpperCase())
			.map( palabra -> palabra+"-"+palabra)
			.subscribe( palabra -> System.out.println(palabra));
		
		
		//////////////
		// FLAT MAP //
		//////////////
		
		System.out.println("======================================");		
		flujos.leerFichero("imagen.bmp")
			.flatMap( contenido -> {
				System.out.println("Contenido del fichero:"+contenido);
				return flujos.conversorImagen(contenido);
			})
			.flatMap( imagenConvertida -> {
				System.out.println("Imagen convertida:"+imagenConvertida);
				return flujos.escribirFichero("imagen.jpg");
			})
			.doOnSuccess( x -> {
				System.out.println("FIN:"+x);
			})
			.subscribe();
		
		
		////////////
		// FILTER //
		////////////

		System.out.println("======================================");		
		peliculaRepo
			.findAll()
			.filter(p -> p.getGenero().equals("Ci-fi"))
			.subscribe(p -> System.out.println(p));
		
		
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
