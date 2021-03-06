package com.curso.controlador;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.negocio.GestorPeliculas;
import com.curso.modelo.persistencia.PeliculaRepositorio;

import reactor.core.publisher.Mono;

@Controller
public class ControladorPeliculas {

	@Autowired private PeliculaRepositorio peliculaRepo;
	@Autowired private GestorPeliculas gestorPeliculas;
	
	@GetMapping("/peliculas")
	public String verPaginaPeliculas(Model model) {
		
		//IReactiveDataDriverContextVariable peliculasFlux = new ReactiveDataDriverContextVariable(peliculaRepositorio.findAll());
		//model.addAttribute("peliculas", peliculasFlux);		
		
		//Si nos subscribimos...mala idea
		/*
		peliculaRepositorio
			.findAll()
			//Si hacemos esto se devuelve la respuesta antes de que termine de ejecutarse el select
			//.subscribeOn(Schedulers.boundedElastic())
			.collect(Collectors.toList())
			.subscribe( peliculas -> {
				System.out.println("DEMASIADO TARDE");
				model.addAttribute("peliculas", peliculas);
			});*/
		
		//Si añadimos al model un flujo/mono es thymeleaf el que se subscribe
		model.addAttribute("peliculas", peliculaRepo.findAll());
		model.addAttribute("pelicula", new Pelicula());
		return "peliculas";
	}	

	//En el modo 'FULL' thymeleaf se subscribe a todos los monos/flujos que le pasamos
	//a traves del modelo y hasta que no terminen esas subscripciones no devuelve
	//el HTML del tirón
	//Todas las subscripciones van con el mismo hilo
	@GetMapping("/seleccionarPelicula_Full")
	public String buscarPelicula_Full(@RequestParam("id") Integer id, Model model) {
		model.addAttribute("peliculas", peliculaRepo.findAll()); //Flujo
		model.addAttribute("pelicula", peliculaRepo.findById(id)); //Mono
		return "peliculas";
	}	

	//CHUNKED: En vez de crear toda la respuesta en memoria (como en FULL) se van enviando
	//fragmentos según se llena un buffer más pequeño
	//En application.properties/yml
	//spring.thymeleaf.reactive.max-chunk-size=1024
	
	@GetMapping("/seleccionarPelicula_Data_Driven")
	public String buscarPelicula_Data_Driven(@RequestParam("id") Integer id, Model model) {
		
		//
		//Solo podemos añadir un IReactuveDataDriverContextVariable al model
		//
		
		//IReactiveDataDriverContextVariable peliculasFlux = 
		//	new ReactiveDataDriverContextVariable(
		//		peliculaRepositorio
		//			.findAll()
		//			.delayElements(Duration.ofSeconds(1)), 1);
		IReactiveDataDriverContextVariable peliculasFlux = 
			new ReactiveDataDriverContextVariable(peliculaRepo.findAll());
		model.addAttribute("peliculas", peliculasFlux); //DataDriven
		model.addAttribute("pelicula", peliculaRepo.findById(id)); //Mono
		return "peliculas";
	}	
	
	@PostMapping("/insertarPelicula")
	public Mono<String> insertarPelicula(@ModelAttribute(name = "pelicula") Pelicula pelicula, Model model) {
		System.out.println("===============================");
		System.out.println("ControladorPeliculas.insertar:"+Thread.currentThread().getName());
		System.out.println("Insertando:"+pelicula);

		//POST-REDIRECT-GET
		return gestorPeliculas
			.insertar(pelicula)
			.thenReturn("redirect:peliculas")
			.onErrorResume(Exception.class, e -> {
				IReactiveDataDriverContextVariable peliculasFlux = new ReactiveDataDriverContextVariable(peliculaRepo.findAll());
				model.addAttribute("peliculas", peliculasFlux); //DATA_DRIVEN
				model.addAttribute("pelicula", pelicula);
				model.addAttribute("error", e.getMessage());
				return Mono.just("peliculas");
			});
	}	

	@PostMapping("/borrarPelicula")
	public Mono<String> borrarPelicula(@ModelAttribute(name = "pelicula") Pelicula pelicula) {
		System.out.println("===============================");
		System.out.println("ControladorPeliculas.borrar:"+Thread.currentThread().getName());
		System.out.println("Borrando:"+pelicula);
		
		return gestorPeliculas
			.borrar(pelicula)
			.thenReturn("redirect:peliculas");
	}	
	
	
}
