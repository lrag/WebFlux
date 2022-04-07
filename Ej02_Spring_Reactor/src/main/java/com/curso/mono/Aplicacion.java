package com.curso.mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class Aplicacion implements CommandLineRunner{

	@Autowired
	private Monos monos;
	
	public static void main(String[] args) {
		SpringApplication.run(Aplicacion.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("=====================================");
		String saludo = monos.saludo();
		System.out.println(saludo);
		
		Mono<String> mono1 = monos.saludoMono();
		mono1.subscribe( str -> System.out.println(str) );
		System.out.println("UNO");
		
		mono1 = monos.saludoMono();
		mono1.subscribeOn(Schedulers.boundedElastic()).subscribe( str -> System.out.println(str) );
		System.out.println("DOS");
		
		Thread.sleep(1000);		
	}

}
