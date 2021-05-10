package com.curso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Aplicacion {

	public static void main(String[] args) {
		
		ApplicationContext appCtx = SpringApplication.run(Aplicacion.class, args);
		
	}

}
