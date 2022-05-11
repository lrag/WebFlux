package com.curso.flux_3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

//@SpringBootApplication
public class MultiplesSubscriptores implements CommandLineRunner{

	public Flux<String> flujoConEstado(String nombre){
		return Flux.generate(
			//State supplier
			() -> 1,
			//Generator
			(state, consumidores) -> {
				consumidores.next(Thread.currentThread().getName()+"-"+nombre+"-Mensaje:"+state);
				state++;				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(state == 11) {
					consumidores.complete();
				}
				
				return state;
			}
		);		
	}	
	
	public static void main(String[] args) {
		SpringApplication.run(MultiplesSubscriptores.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	
		/*
		//
		//Dos flujos distintos, con una subscripción cada uno
		//
		System.out.println("=========================================================");
		Flux<String> flujo1 = flujoConEstado("A");
		Disposable d1 = flujo1
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe( elemento -> System.out.println(Thread.currentThread().getName()+"-Consumidor1:"+elemento));
		
		Flux<String> flujo2 = flujoConEstado("B");
		Disposable d2 = flujo2
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe( elemento -> System.out.println(Thread.currentThread().getName()+"-Consumidor2:"+elemento));

		Thread.sleep(20_000);	
		d1.dispose();
		d2.dispose();
		*/
		
		//
		//Uno flujo, dos subscripciones
		//
		/*
		System.out.println("=========================================================");
		Flux<String> flujo3 = flujoConEstado("C");
		Disposable d3 = flujo3
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe( elemento -> System.out.println(Thread.currentThread().getName()+"-Consumidor3:"+elemento));
		Disposable d4 = flujo3
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe( elemento -> System.out.println(Thread.currentThread().getName()+"-Consumidor4:"+elemento));
		
		Thread.sleep(20_000);	
		d3.dispose();
		d4.dispose();
		*/
		
		//
		//Uno flujo, dos subscripciones
		//
		System.out.println("=========================================================");
		Flux<String> flujo4 = flujoConEstado("D");
		Disposable d5 = flujo4
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe( elemento -> {
				System.out.println(Thread.currentThread().getName()+"-Consumidor5:"+elemento);
			});
		Disposable d6 = flujo4
				.subscribeOn(Schedulers.boundedElastic())
				.subscribe( elemento -> {
					System.out.println(Thread.currentThread().getName()+"-Consumidor6:"+elemento);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});

		Thread.sleep(40_000);	
		d5.dispose(); //Esto ya da igual
		d6.dispose();		
	}

}




