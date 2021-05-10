package com.curso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.curso.modelo.persistencia.PeliculaHistoricoRepositorio;
import com.curso.modelo.persistencia.PeliculaRepositorio;

@SpringBootApplication
public class Aplicacion {
	
	

	public static void main(String[] args) {
		ApplicationContext appCtx =  SpringApplication.run(Aplicacion.class, args);
		
		PeliculaRepositorio peliculaRepo = appCtx.getBean(PeliculaRepositorio.class);
		PeliculaHistoricoRepositorio peliculaHistoricoRepo = appCtx.getBean(PeliculaHistoricoRepositorio.class);
		
		//Flux.concat(peliculaRepo.findAll(), peliculaRepo.findById(2)).subscribe( o -> System.out.println(o));
			
		//peliculaRepo.findAll().collect(Collectors.toList()).subscribe(x -> System.out.println(x));
		
		
		//peliculaRepo.findAll().collect(Collectors.toList()).thenReturn(null)
		
		
		System.out.println("===================================");
		peliculaHistoricoRepo.findAll().subscribe(x -> System.out.println(x));
		
		
		
	}

}
