package com.curso.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.negocio.ServicioPeliculas;
import com.curso.modelo.persistencia.PeliculaRepositorio;
import com.curso.modelo.persistencia.PremioRepositorio;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class PeliculasREST {

	@Autowired private PeliculaRepositorio peliculaRepo;
	@Autowired private PremioRepositorio premioRepo;
	@Autowired private ServicioPeliculas servicioPeliculas;
	
	//GET    /peliculas   
	//GET    /peliculas/{id}
	//POST   /peliculas
	//PUT    /peliculas/{id}
	//DELETE /peliculas/{id}  
	
	//No es obligatorio que un método del endpoint devuelva un flujo o un mono.
	//Puede ser imperativo mientras no estemos bloqueando el hilo del event loop
	@GetMapping(path = "/peliculas_destructor_de_la_reactividad",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Pelicula> listarPeliculas_destructor_de_la_reactividad() {	
		//Esto es aberrante
		//Muerte y destrucción
		List<Pelicula> pelis = new ArrayList<>();
		//Aqui estamos forzando al hijo del event loop a ejecutar una consulta a la bb.dd
		peliculaRepo.findAll().subscribe( p -> pelis.add(p) );
		//Aqui estamos usando otro hilo, pero entonces el del event loop se vuelve con una lista vacía
		//peliculaRepo.findAll().subscribeOn(Schedulers.boundedElastic()).subscribe( p -> pelis.add(p) );
		//Lo mismo, pero con blocl:
		//return peliculaRepo.findAll().collect(Collectors.toList()).block();

		return pelis;
	}
		
	@GetMapping(path = "/peliculas",
			    produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Pelicula> listarPeliculas_Clientes_No_Reactivos() {	
		return peliculaRepo.findAll();
	}
	
	@GetMapping(path = "/peliculas_stream",
			produces = MediaType.TEXT_EVENT_STREAM_VALUE)
			//produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Flux<Pelicula> listarPeliculas_Clientes_Reactivos() {	
		return peliculaRepo.findAll();
	}
	
	@GetMapping(path="/peliculas/titulos",
			    produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<List<String>> listarTitulos(){
		return peliculaRepo.findAll() //de aqui sale Flux<Pelicula>
			.map(pelicula -> pelicula.getTitulo())
			.collectList(); //de aqui sale Mono<List<Pelicula>>;
	}
	
	@GetMapping(path="/peliculas/titulos_stream",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<String> listarTitulosStream(){
		return peliculaRepo.findAll() //de aqui sale Flux<Pelicula>
			.map(pelicula -> pelicula.getTitulo()); //de aqui sale Flux<String>
	}
	
	@GetMapping(
		path = "/peliculas/{idPelicula}",
		produces = MediaType.APPLICATION_JSON_VALUE
	)	
	public Mono<Pelicula> buscarPelicula(@PathVariable("idPelicula") Integer idPelicula) {
		return peliculaRepo.findById(idPelicula);	
	}
	
	@GetMapping(
		path = "/peliculas/{idPelicula}/premios",
		produces = MediaType.APPLICATION_JSON_VALUE
	)	
	public Mono<Pelicula> buscarPeliculaConPremios(@PathVariable("idPelicula") Integer idPelicula) {
		/*
		Pelicula p = repoPeliculas.buscar(idPelicula);
		List<Premio> premios = repoPremios.buscarPorPelicula(idPelicula);
		p.setPremios(premios);
		return p;
		*/		
		return peliculaRepo
			.findById(idPelicula) //Mono<Pelicula>
			.flatMap( p -> {
				return premioRepo.findAllByIdPelicula(p.getId()).collectList().map( lista -> {
					p.setPremios(lista);
					return p;
				});
			});
	}
	
	@PostMapping(path = "/peliculas",
			     consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Pelicula> insertarPelicula(@RequestBody Pelicula pelicula) {
		return servicioPeliculas.insertar(pelicula);
	}
	
	@PutMapping(path = "/peliculas/{id}")
	public Mono<Pelicula> modificarPelicula(@RequestBody Pelicula pelicula, @PathVariable("id") Integer id) {
		return servicioPeliculas.modificar(pelicula);
	}
	
	@DeleteMapping(path = "/peliculas/{id}")
	public Mono<Void> borrarPelicula(@PathVariable("id") Integer id) {
		return servicioPeliculas.borrar(id);
	}
	
	/////////////////////////////////////////
	
	@PostMapping(path="sumar")
	public Double sumar(@RequestParam("sumando1") Double sumando1, @RequestParam("sumando2") Double sumando2) {
		//Este código se ejecuta muy rápido y no estamo bloqueando al hilo del event loop
		return sumando1 + sumando2;	
	}

	public Mono<Double> sumar_reactivo(@RequestParam("sumando1") Double sumando1, @RequestParam("sumando2") Double sumando2) {
		//Aqui quizas estemos matando moscas a cañonazos
		return Mono.create(subscriptores -> subscriptores.success(sumando1 + sumando2));
		//Mono.fromCallable(null)
		//Mono.fromRunnable(null)
	}	
	
	public Byte[] convertirImagen_Bloqueante(Byte[] imagen) {
		//llamada a la lógica de conversión, que está un buen rato con ello aunque no haya I/O 
		//Aqui estamos 'bloqueando' al event loop con la conversión de la imagen
		return ConversorImagenes.convertir(imagen);
	}
		
	public Mono<Byte[]> convertirImagen_Reactivo(Byte[] imagen) {
		//llamada a la lógica de conversión, que está un buen rato con ello
		//Aqui estamos 'bloqueando' al event loop con la conversión de la imagen
		//return ConversorImagenes.convertir(imagen);
		
		Mono<Byte[]> chimpance = Mono.create( subcriptores -> {
			Byte[] nuevaImagen = ConversorImagenes.convertir(imagen);
			subcriptores.success(nuevaImagen);
		});
		return chimpance;	
	}
	
}

class ConversorImagenes {
	public static Byte[] convertir(Byte[] imagen) {
		//Supongamos que esto dedica 5 segundos a procesar la imagen y convertirla...
		return null;
	}
}
