package com.curso.flux_4_operadores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.entidad.Premio;

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

		////////////
		// CONCAT //
		////////////
		/*
		System.out.println("======================================");
		Flux.concat(
				//Primero se subscriben a este flujo y lo consumen entero
				flujos.numerosPares().subscribeOn(Schedulers.boundedElastic()),
				//Luego se subscriben a este
				flujos.numerosImpares().subscribeOn(Schedulers.boundedElastic())
			)
			.subscribe( n -> System.out.println(Thread.currentThread().getName()+":"+n));
		
		Thread.sleep(20_000);
		*/
		
		/*
		System.out.println("======================================");
		//Tambien podemos concatenar varios monos para obtener un flujo
		Flux.concat(
				peliculaRepo.findById(1), 
				peliculaRepo.findById(2), 
				peliculaRepo.findById(3)
			)
			.subscribe(p -> System.out.println(p));
		
		Thread.sleep(20_000);
		*/
		
		/*
		System.out.println("======================================");
		peliculaRepo
			.findAllById(Arrays.asList(1,2,3))
			.subscribe( p -> System.out.println( "peliculaRepo.findAllById:"+p));
		
		Thread.sleep(20_000);		
		*/
		
		///////////
		// MERGE //
		///////////
		/*
		System.out.println("======================================");
		Flux.merge(
				flujos.numerosPares().subscribeOn(Schedulers.boundedElastic()), 
				flujos.numerosImpares().subscribeOn(Schedulers.boundedElastic())
			)
			.subscribe( n -> System.out.println(Thread.currentThread().getName()+":"+n));
		
		Thread.sleep(10_000);

		System.exit(0);
		*/
		
		/////////
		// ZIP //
		/////////
		/*
		System.out.println("======================================");		
		Flux.zip(
				//Esto han de ser monos
				flujos.numerosPares().subscribeOn(Schedulers.boundedElastic()).collect(Collectors.toList()), 
				flujos.numerosImpares().subscribeOn(Schedulers.boundedElastic()).collect(Collectors.toList())
			)
			//El consumidor recibe un único elemento que contiene lo que hayan entregado los monos anteriores
			.subscribe( tupla -> {
				System.out.println(tupla.getT1());
				System.out.println(tupla.getT2());
			});		

		Thread.sleep(15_000);
		System.exit(0);
		*/		
		
		////////////
		// FILTER //
		////////////


		System.out.println("======================================");		
		/*
		peliculaRepo
			.findAll()
			.filter(p -> p.getGenero().equals("Ci-fi"))
			//Se pueden concatenar más filtros
			.filter(p -> p.getTitulo().length()>4)
			.subscribe(p -> System.out.println(p));	
		System.exit(0);
		*/

		
		/////////
		// MAP //
		/////////
		/*
		System.out.println("======================================");
		flujos
			.flujoPalabras()
			.subscribe( palabra -> System.out.println(palabra));
		
		System.out.println("======================================");
		flujos
			.flujoPalabras()
			//Llega un string, sale otro
			.map( p -> p.toUpperCase() )
			.subscribe(palabra -> System.out.println(palabra));


		System.out.println("======================================");
		flujos
			.flujoPalabras()
			//Llega la palabra, sale su longitud...
			.map( p -> p.length() )
			.subscribe(longitud -> System.out.println(longitud));
		*/
		

		/*
		System.out.println("======================================");
		flujos
			.flujoPalabras() //De aqui salen cadenas de texto
			.map( palabra -> palabra.toUpperCase() ) //De aqui salen pasadas a mayusculas
			.map( palabra -> palabra.length()+"-"+palabra) //De aqui salen con longitud por delante
			.subscribe( palabra -> System.out.println(palabra));		
		
		System.out.println("======================================");
		//Lo mismo pero sin 'map'
		flujos
			.flujoPalabras()
			.subscribe( palabra -> System.out.println(palabra.length()+":"+palabra.toUpperCase()));		
		*/
		
		//////////////
		// FLAT MAP //
		//////////////
		
		//System.out.println("======================================");
		//leer un fichero imagen
		//transformar el contendio en otro formato
		//crear un nuevo fichero con la imagen resultante
		
		//Imperativo: 3 líneas
		//String contenido = leerFichero(nombreFichero);
		//String nuevoFormato = convertirImagen(contenido);
		//escribirFichero(nuevoFichero, nuevoFormato);
		
		//Con callback hell el código es un infierno
		//Además, como nos subscribimos a los flujos/monos no podemos devolvelos
		//Y tampoco podemos devolver el resultado si el código está en un método
		//Y para más INRI no podremos avisar de que ha habido un fallo

		/*
		flujos
			.leerFichero("imagen.jpg") 
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe( contenido -> {
				System.out.println("Contenido:"+contenido);
				flujos
					.convertirImagen(contenido)
					.subscribeOn(Schedulers.boundedElastic())
					.subscribe(nuevoFormato -> {
						System.out.println("Nuevo formato:"+nuevoFormato);
						flujos.escribirFichero("imagenConvertida.jpg", nuevoFormato)
							.subscribeOn(Schedulers.boundedElastic())
							.subscribe( c -> {
								//Este consumer nunca recibirá una llamada por que nos estamos subscribiendo a un Mono<Void>
								System.out.println("Nueva imagen creada.");
							});
					});					
			});	
		Thread.sleep(10_000);
		*/
		
		//System.exit(0);
		 
		/*
		flujos
			.leerFichero("imagen.jgp") //De aqui sale un mono<string>
			.flatMap( contenido -> {
				System.out.println("Contenido:"+contenido);
				return flujos.convertirImagen(contenido); //Esto devuelve  Mono<String> 
			})
			.flatMap( nuevoFormato -> {
				System.out.println("Nuevo formato:"+nuevoFormato);
				return flujos.escribirFichero("imagenConvertida.jpg", nuevoFormato); //De aqui sale un Mono<Void>
			})
			.doOnSuccess( x -> System.out.println("IMAGEN CONVERTIDA") ) //Como el ultimo mono es Mono<Void> 'x' es null
			.subscribe();	
		*/
		
		

		//Lo mismo pero con un hilo para cada tarea que implique I/O 
		/*
		flujos
			.leerFichero("imagen.jgp") //De aqui sale un mono
			.subscribeOn(Schedulers.boundedElastic())
			.flatMap( contenido -> {
				System.out.println(Thread.currentThread().getName()+"-Contenido:"+contenido);
				return flujos
					.convertirImagen(contenido)
					.subscribeOn(Schedulers.boundedElastic()); //Esto devuelve un Mono<String> 
			})
			.flatMap( nuevoFormato -> {
				System.out.println(Thread.currentThread().getName()+"-Nuevo formato:"+nuevoFormato);
				return flujos
					.escribirFichero("imagenConvertida.jpg", nuevoFormato)
					.subscribeOn(Schedulers.boundedElastic());
			})
			.doOnSuccess( x-> System.out.println(Thread.currentThread().getName()+"-IMAGEN CONVERTIDA") )
			.subscribe();
		
		Thread.sleep(10_000);
		 */
		
		System.out.println("======================================");	
		
		//Podemos hacer esta ñapa un tanto espantosa:
		Pelicula peliculaConPremios = new Pelicula();
		
		peliculaRepo
			.findById(3) //De aqui sale un mono de pelicula sin los premios
			.flatMap(pelicula -> {
				peliculaConPremios.setId(pelicula.getId());
				peliculaConPremios.setTitulo(pelicula.getTitulo());
				peliculaConPremios.setDirector(pelicula.getDirector());
				peliculaConPremios.setGenero(pelicula.getGenero());
				peliculaConPremios.setYear(pelicula.getYear());
				return premioRepo.findAllByIdPelicula(3).collect(Collectors.toList()); //De aqui sale un flujo de premios
			})
			.subscribe(premios -> {
				peliculaConPremios.setPremios(premios);
				System.out.println(peliculaConPremios);
			});

		//O con zip, que no es ñapa pero tampoco es que sea muy bonito
		Integer idPelicula = 3;
		peliculaRepo
			.findById(idPelicula) //De aqui sale un Mono en patines
			.flatMap( p -> { //Aqui llega la pelicula
				Mono<List<Premio>> monoPremios = premioRepo.findAllByIdPelicula(p.getId()).collectList();
				Mono<Pelicula> peliculaMono = Mono.just(p);
				return Mono.zip(peliculaMono, monoPremios);
				//return Mono.just(p).zipWith(premioRepo.findAllByIdPelicula(p.getId()).collectList()); 
			})
			.map( tupla -> {
				Pelicula p = tupla.getT1();
				p.setPremios(tupla.getT2());
				return p;
			})
			.subscribe(pelicula -> System.out.println(pelicula));			

		///////////////////
		// FLAT MAP MANY //
		///////////////////			
		//...		
		
		/////////////
		// COLLECT //
		/////////////
		/*
		System.out.println("======================================");		
		flujos
			.flujoPalabras()
			//.collect(Collectors.toList())
			.collectList() //Lo mismo
			.subscribe(lista -> System.out.println(lista));
		*/
		/*
		peliculaRepo
			.findAll()
			.collect(Collectors.groupingBy( pelicula -> pelicula.getGenero()))
			.subscribe( mapa -> {
				mapa.forEach( (clave,valor) -> System.out.println(clave+":"+valor) );
			});*/
		
		/*
		.defaultIfEmpty(valor por defecto);
		.switchIfEmpty(Mono.error(new Exception("EL AÑO NO PUEDE SER NEGATIVO!")));
		*/
		
	}
	
}

