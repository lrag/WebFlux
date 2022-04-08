package com.curso.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.negocio.GestorPeliculas;
import com.curso.modelo.persistencia.PeliculaRepositorio;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PeliculasREST {

	@Autowired private PeliculaRepositorio peliculaRepo;
	@Autowired private GestorPeliculas gestorPeliculas;
	
	//GET    /peliculas   
	//GET    /peliculas/{id}
	//POST   /peliculas
	//PUT    /peliculas/{id}
	//DELETE /peliculas/{id}  
	
	@GetMapping(path = "/peliculas",
			    produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Pelicula> listarPeliculas() {	
		
		//Esto es aberrante
		//Muerte y destrucción
		//List<Pelicula> pelis = new ArrayList<>();
		//peliculaRepo.findAll().subscribe( p -> pelis.add(p) );
		//return pelis;
		
		return peliculaRepo.findAll();
	}
		
	@GetMapping(path = "/peliculas/{id}")	
	public Mono<Pelicula> buscarPelicula(@PathVariable("id") Integer id) {
		return peliculaRepo.findById(id);
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
	
}


