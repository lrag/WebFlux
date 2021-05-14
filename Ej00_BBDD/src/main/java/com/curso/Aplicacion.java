package com.curso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.curso.modelo.entidad.Pelicula;
import com.curso.modelo.entidad.Premio;
import com.curso.modelo.persistencia.PeliculaRepositorio;
import com.curso.modelo.persistencia.PremioRepositorio;

@SpringBootApplication
public class Aplicacion {

	public static void main(String[] args) {
		
		ApplicationContext appCtx = SpringApplication.run(Aplicacion.class, args);
		
		PeliculaRepositorio pr = appCtx.getBean(PeliculaRepositorio.class);
	
		pr.deleteAll();
		
		pr.save(new Pelicula(null,"2001","D1","Ci-fi",2000));
		pr.save(new Pelicula(null,"Alien","D1","Ci-fi",2000));
		pr.save(new Pelicula(null,"Die Hard","D1","Accion",2000));
		pr.save(new Pelicula(null,"Young Frankenstein","D1","Comedia",2000));
		pr.save(new Pelicula(null,"Los violentos de Kelly","D1","Bélica",2000));
		
		PremioRepositorio premioRepo = appCtx.getBean(PremioRepositorio.class);
		premioRepo.deleteAll();
		
		premioRepo.save(new Premio(null, "Mejor pelicula de accion de todos los tiempos", "1234", 3));
		premioRepo.save(new Premio(null, "Yippee ki yay", "4321", 3));
		premioRepo.save(new Premio(null, "Mejor pelicula en la que sale Carl Winslow", "5678", 3));
		
	}

}
