package com.curso;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class Aplicacion implements CommandLineRunner{

	@Autowired
	private Flujos flujos;
	
	//@Autowired
	//private Consumidor consumidorString;
	
	public static void main(String[] args) {
		SpringApplication.run(Aplicacion.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//En un stream del api de colecciones los elementos ya existen antes de que salga el primero
		List<String> textos = flujos.listarPalabras();		
		textos.stream().forEach( texto -> System.out.println("Texto:"+texto ));
		
		System.out.println("=====================================");
		Flux<String> flujoString = flujos.listarPalabrasFlux();
		
		//Un flujo no se 'recorre' como si fuera una colección
		//Se proporciona un consumidor, que recibirá los elementos que componen el flujo
		//Es tarea del consumidor controlar el ritmo con el que procesa los elementos
		//Es tarea del flujo controlar el ritmo con el que se entregan los elementos
		
		//
		//Proporcionando un consumidor definido en el momento de la subscripción:
		//En este caso el mismo hilo que se subscribe ejecuta el código del consumidor
		//
		
		System.out.println("Antes de subscribirse");
		flujoString
			.subscribe(new Consumer<String>() {
				@Override
				public void accept(String s) {
					System.out.println(Thread.currentThread().getName()+"-"+s);
				}
			});
		System.out.println("Despues de subscribirse");

		
		System.out.println("=====================================");
		//Ídem con expresión lambda
		flujoString.subscribe(s -> System.out.println(Thread.currentThread().getName()+"-Con lambda:"+s));

		
		/*
		System.out.println("=====================================");		
		//Utilizando un consumidor definido como una bean de spring
		Flux<String> flujoGenerado = flujos.flux4();		
		flujoGenerado.subscribe(consumidorString);
		*/
		
		System.out.println("=====================================");
		Flux<String> flujoGenerado = flujos.flux4();		
		flujoGenerado.subscribe(m -> System.out.println(m));
		
		Thread.sleep(20000);
		
		System.exit(0);
		
		
		System.out.println("=====================================");
		//		
		//Podemos subscribirnos a un flujo y cancelar esa subscripción en cualquier momento
		//Observar que la ejecución del código del consumidor se le encarga a otro hilo
		//
		System.out.println(Thread.currentThread().getName());
		Flux<Long> fluxInfinito = flujos.flux2();
		
		Disposable d = 
			fluxInfinito
			.subscribe(l -> {
				System.out.println(Thread.currentThread().getName()+":"+l);
			});
		
		//Thread.sleep(5000);		
		
		System.out.println("Cancelando la subscripción...");
		d.dispose();
		
		//El hilo asignado al consumidor no tiene peso suficiente para mantener viva a la aplicación	
		//Thread.sleep(5000);

		/*
		System.out.println("=====================================");
		System.out.println("Números aleatorios...");
		Disposable d2 = flujos.flux6()
			.subscribe(s -> System.out.println(s));
		Thread.sleep(10000);
		d2.dispose();	
		*/	
		
		
		System.out.println("=====================================");
		System.out.println("Ficheros...");
		Disposable d3 = flujos.flux5()
			.subscribeOn(Schedulers.boundedElastic()) //Que pasa si comentamos esta línea
			.subscribe(s -> System.out.println(s));
		
		Thread.sleep(200000);
		d3.dispose();
		
		
		/*
		//
		//Con subscribeOn podemos indicar que queremos otro hilo para ejecutar
		//el codigo del consumidor
		//
		System.out.println("=====================================");
		System.out.println("Antes de subscribeOn");
		System.out.println(Thread.currentThread().getName());
		
		flujoString
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe(str -> {
				System.out.println(Thread.currentThread().getName()+":"+str);
				
				long inicio = System.currentTimeMillis();
				while(System.currentTimeMillis()<inicio+2000) {
					//Estoy haciendo cosas 
				}			
			});
		
		flujoString
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe(str -> {
				System.out.println(Thread.currentThread().getName()+":"+str);
	
				long inicio = System.currentTimeMillis();
				while(System.currentTimeMillis()<inicio+2000) {
					//Más cositas 
				}
			});
		
		System.out.println("Después de subscribeOn");
		Thread.sleep(20000);
		System.exit(0);
		
		
		//
		//Si necesitamos que los elementos del flujo se procesen en paralelo:
		//
		System.out.println("=====================================");
		System.out.println("Parallel flux");
		flujoString
			.parallel()
			.runOn(Schedulers.parallel())
			.subscribe(str -> {
				System.out.println(Thread.currentThread().getName()+":"+str);
				long inicio = System.currentTimeMillis();
				while(System.currentTimeMillis()<inicio+2000) {
					//Estoy haciendo cosas
				}
			});
		

		//	
		//El propio flujo puede indicar que cada elemento debe procesarse en un hilo distinto que el que
		//se subscribe:
		//
		System.out.println("=====================================");
		System.out.println("publishOn");
		Flux<String> flujoPublishOn = flujos.flux3();
		System.out.println("Esta llamada a subscribe no es síncrona.");
		flujoPublishOn
			.subscribe(str -> {
				System.out.println(Thread.currentThread().getName()+":"+str);
				//long inicio = System.currentTimeMillis();
				//while(System.currentTimeMillis()<inicio+2000) {
					//Estoy haciendo cosas
				//}
			});
		System.out.println("El hilo que se subscribe llega aquí antes de que se procesen los elementos");
		
		
		//
		//Es posible que el flujo tenga 'publishOn' pero el consumidor necesite
		//que el hilo que se subscribe tambien procese los elementos
		//
		System.out.println("=====================================");
		System.out.println("Antes");
		flujoString
		.subscribeOn(Schedulers.immediate()) //Ignoramos que el flujo tiene publishOn(...)
		.subscribe(str -> {
			System.out.println(Thread.currentThread().getName()+":"+str);

			long inicio = System.currentTimeMillis();
			while(System.currentTimeMillis()<inicio+2000) {
				//Más cositas 
			}
		});		
		System.out.println("Despues");
		
		*/
		
		System.out.println("FIN del hilo main");
		
	}

}


