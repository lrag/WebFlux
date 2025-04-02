package com.curso.flux_1;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class Monos {

	public String saludar(){
		return "HOLA!";
	}	
	
	public Mono<String> saludar_reactivo(){
		return Mono.just("HOLA, SOY UN MONO!");
	}
	
}
