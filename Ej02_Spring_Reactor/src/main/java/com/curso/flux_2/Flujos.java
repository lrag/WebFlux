package com.curso.flux_2;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
public class Flujos {

	public Flux<Long> fluxNumerosAleatoriosFinito(){
		AtomicInteger contador = new AtomicInteger(0);
		return Flux.generate(
			//Generator
			sink -> {
				Long numero = Math.round(Math.random()*10_000);
				sink.next(numero);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(contador.incrementAndGet()==10) {
					//Indicamos al subscriptor que hemos terminado
					sink.complete();
				}
			}				
		);
	}	
	
	public Flux<String> flujoConEstado(){
		Flux<String> flux = Flux.generate(
			//State supplier
				//Solo se invoca una vez, antes de entregar el primer elemento al subscriptor
			() -> 1, 
			//Generator
			(state, sink) -> {
				sink.next("Mensaje número: "+state);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(state>9) {
					sink.complete();
				} 				
				return state+1;
			} 
		);	
		return flux;
	}
	
}

