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
import com.curso.modelo.negocio.GestorPeliculas;
import com.curso.modelo.persistencia.PeliculaRepositorio;
import com.curso.modelo.persistencia.PremioRepositorio;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PeliculasREST {

	@Autowired private PeliculaRepositorio peliculaRepo;
	@Autowired private PremioRepositorio premioRepo;
	@Autowired private GestorPeliculas gestorPeliculas;
	
	//GET    /peliculas   
	//GET    /peliculas/{id}
	//POST   /peliculas
	//PUT    /peliculas/{id}
	//DELETE /peliculas/{id}  
	
	//No es obligatorio que un método del endpoint devuelva un flujo o un mono.
	//Puede ser imperativo mientras no estemos bloqueando el hilo del event loop
	@GetMapping(path = "/peliculas_destructor_de_la_reactividad",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Pelicula> listarPeliculas_el_horror_el_horror() {	
		//Esto es aberrante
		//Muerte y destrucción
		List<Pelicula> pelis = new ArrayList<>();
		//Aqui estamos forzando al hijo del event loop a ejecutar una consulta a la bb.dd
		peliculaRepo.findAll().subscribe( p -> pelis.add(p) );
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
		
	@GetMapping(path = "/peliculas/{idPelicula}")	
	public Mono<Pelicula> buscarPelicula(@PathVariable("idPelicula") Integer idPelicula) {
		return peliculaRepo
			.findById(idPelicula) //De aqui sale un Mono en patines
			.flatMap( p -> { //Aqui llega la pelicula
				return Mono.just(p).zipWith(premioRepo.findAllByIdPelicula(p.getId()).collectList()); 
			})
			.map( tupla -> {
				Pelicula p = tupla.getT1();
				p.setPremios(tupla.getT2());
				return p;
			});	
	}
	
	@PostMapping(path = "/peliculas",
			     consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Pelicula> insertarPelicula(@RequestBody Pelicula pelicula) {
		return gestorPeliculas.insertar(pelicula);
	}
	
	@PutMapping(path = "/peliculas/{id}")
	public Mono<Pelicula> modificarPelicula(@RequestBody Pelicula pelicula, @PathVariable("id") Integer id) {
		return gestorPeliculas.modificar(pelicula);
	}
	
	@DeleteMapping(path = "/peliculas/{id}")
	public Mono<Void> borrarPelicula(@PathVariable("id") Integer id) {
		return gestorPeliculas.borrar(id);
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
