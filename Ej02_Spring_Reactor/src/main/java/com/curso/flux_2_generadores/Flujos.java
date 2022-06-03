package com.curso.flux_2_generadores;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Supplier;
import reactor.core.publisher.Flux;

@Component
public class Flujos {

	/////////////
	// EMITTER //
	/////////////
	public Flux<Long> fluxNumerosAleatoriosFinito_Emitter(){
		Flux<Long> flujo = Flux.create( 
				//el emiter recibe una única llamada
				consumidores -> {					
					int contador = 0;
					
					while(contador<10) {
						System.out.println(Thread.currentThread().getName()+"-Generando número aleatorio...");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}	
					
						consumidores.next(Math.round(Math.random()*10_000));
					}
					consumidores.complete();
				});
		return flujo;
	}
	
	///////////////
	// GENERATOR //
	///////////////
	public Flux<Long> fluxNumerosAleatoriosFinito(){
		AtomicInteger contador = new AtomicInteger(0);
		
		Flux<Long> flujo = Flux.generate( 
				//Generator
				consumidores -> {
					System.out.println(Thread.currentThread().getName()+"-Generando número aleatorio...");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
					
					consumidores.next(Math.round(Math.random()*10_000));
					if(contador.incrementAndGet() == 10) {
						//Indicamos al subscriptor que hemos terminado
						consumidores.complete();
					}
				});
		return flujo;
	}
		
	public Flux<String> flujoConEstado(){
		Flux<String> flux = Flux.generate(
			//State supplier
			() -> 1,
			//Generator
			(state, consumidores) -> {

				consumidores.next("Mensaje nº:"+state);
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
		return flux;
	}
	
	public Flux<String> flujoConEstadoYStateConsumer(){
		Flux<String> flux = Flux.generate(
			//State supplier
			() -> 1,
			//Generator
			(state, consumidores) -> {

				consumidores.next("Mensaje nº:"+state);
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
			},
			//State consumer
			(state) -> System.out.println("E TERMINAO")
		);		
		return flux;
	}	
	
}



