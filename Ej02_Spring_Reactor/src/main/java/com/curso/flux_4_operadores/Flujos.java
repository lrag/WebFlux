package com.curso.flux_4_operadores;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class Flujos {
	
	public Flux<Integer> numerosPares(){
		return Flux.generate(
				//State supplier
				() -> 0,
				//Generator
				(state,consumidores) -> {					
					consumidores.next(state);					
					long fin = System.currentTimeMillis()+1000;
					while(System.currentTimeMillis()<fin) {
					}
					state +=2;
					if(state > 12) {
						consumidores.complete();
					}					
					return state;
				}				
			);
	}

	public Flux<Integer> numerosImpares(){
		return Flux.generate(
				//State supplier
				() -> 1,
				//Generator
				(state,consumidores) -> {					
					consumidores.next(state);					
					long fin = System.currentTimeMillis()+1000;
					while(System.currentTimeMillis()<fin) {
					}
					state +=2;
					if(state > 13) {
						consumidores.complete();
					}					
					return state;
				}				
			);
	}	
	
	public Flux<String> flujoPalabras() {		
		String[] palabras = {"Dice","que…","la","parte","contratante","de","la","primera","parte","será","considerada","como","la","parte","contratante","de","la","primera","parte."};		
		return Flux.generate(
				//State supplier
				() -> 0,
				//Generator
				(state, subscriptores) -> {					
					subscriptores.next(palabras[state]);					
					try {
						Thread.sleep(palabras[state].length()*30);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}					
					state++;
					if(state == palabras.length) {
						subscriptores.complete();
					}					
					return state;
				}				
			);
	}	
	
	public Mono<String> leerFichero(String fichero){
		
		//return Mono.fromRunnable(() -> {});
		//return Mono.fromCallable(() -> null);
		
		return Mono.create( 
				subscriptores -> {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					subscriptores.success("contenido del fichero");
				}
			);
	}
	
	public Mono<Void> escribirFichero(String fichero, String contenido){
		return Mono.create( 
				subscriptores -> {
					System.out.println("Escribiendo el fichero...");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Escribiendo... Contenido:"+contenido+", fichero:"+fichero);
					subscriptores.success();
				}
			);
	}	
	
	public Mono<String> convertirImagen(String imagen){
		return Mono.create( 
				subscriptores -> {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					subscriptores.success(imagen.toUpperCase());
				}
			);
	}
		
}
