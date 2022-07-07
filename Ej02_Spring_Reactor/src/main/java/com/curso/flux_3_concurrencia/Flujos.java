package com.curso.flux_3_concurrencia;

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
	
	//Un flujo que emite un elemento cada cierto tiempo
	public Flux<Long> fluxInterval(){
		Flux<Long> flux = Flux.interval(Duration.ofSeconds(1));
		return flux;
	}	
		
	public Flux<Long> fluxNumerosAleatoriosInfinito(){
		return Flux.generate(
			//Generator
			subscriptores -> {
				System.out.println(Thread.currentThread().getName()+"-Creando numero aleatorio");
				Long numero = Math.round(Math.random()*10_000);
				subscriptores.next(numero);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}				
		);
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

	public Flux<List<String>> monitorizarDirectorio() throws IOException{				
		return Flux.generate(
			consumidores -> {				
				try {
					WatchKey key = null;
					final Path path = Paths.get("directorio_monitorizado");
					final WatchService watchService = FileSystems.getDefault().newWatchService();
					path.register(
							watchService, 
							StandardWatchEventKinds.ENTRY_CREATE, 
							StandardWatchEventKinds.ENTRY_DELETE, 
							StandardWatchEventKinds.ENTRY_MODIFY
						);		

					System.out.println("Esperando a una accion en el directorio para publicar el siguiente mensaje...");
					key = watchService.take();
					List<String> mensaje = new ArrayList<>();
					//el método 'pollEvents' bloquea al hijo que lo invoca hasta que haya un nuevo evento
					for (WatchEvent<?> event : key.pollEvents()) {
						mensaje.add(event.kind()+":"+event.context());
					}
					consumidores.next(mensaje);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			} 
		);
	}

	public Flux<String> flujoPalabras() {		
		String[] palabras = {"UNO","DOS","TRES","CUATRO","CINCO","SEIS","SIETE","OCHO","NUEVE","DIEZ","ONCE","DOCE","TRECE","CATORCE"};		
		return Flux.generate(
				//State supplier
				() -> 0,
				//Generator
				(state,sink) -> {					
					sink.next(palabras[state]);
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
					
					state++;
					if(state == palabras.length) {
						sink.complete();
					}					
					return state;
				}				
			);
	}	
	
	//Flujo que indica que el consumidor procesará los elementos utilizando otro hilo
	public Flux<String> flujoPublishOn() {
		Flux<String> stringFlux = Flux
			.just("siete","caballos","vienen","de","bonanza")
			.publishOn(Schedulers.boundedElastic());
		return stringFlux;
	}	
	
}
