package com.curso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.persistencia.PeliculaRepositorio;

@SpringBootApplication
public class Aplicacion {

	public static void main(String[] args) {
		
		ApplicationContext appCtx = SpringApplication.run(Aplicacion.class, args);
		
		PeliculaRepositorio pr = appCtx.getBean(PeliculaRepositorio.class);
	
		pr.save(new Pelicula(null,"2001","D1","G1",2000));
		pr.save(new Pelicula(null,"Alien","D1","G1",2000));
		pr.save(new Pelicula(null,"Die Hard","D1","G1",2000));
		pr.save(new Pelicula(null,"Young Frankenstein","D1","G1",2000));
		pr.save(new Pelicula(null,"Los violentos de Kelly","D1","G1",2000));
		
	}

}
