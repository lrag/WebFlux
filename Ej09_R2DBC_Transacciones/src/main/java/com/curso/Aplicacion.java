package com.curso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.curso.modelo.negocio.GestorPeliculas;
import com.curso.modelo.persistencia.PeliculaHistoricoRepositorio;
import com.curso.modelo.persistencia.PeliculaRepositorio;

@SpringBootApplication
public class Aplicacion {

	public static void main(String[] args) {
		ApplicationContext appCtx = SpringApplication.run(Aplicacion.class, args);
		
		PeliculaRepositorio peliculaRepo = appCtx.getBean(PeliculaRepositorio.class);
		PeliculaHistoricoRepositorio peliculaHistoricoRepo = appCtx.getBean(PeliculaHistoricoRepositorio.class);
		GestorPeliculas gestorPeliculas = appCtx.getBean(GestorPeliculas.class);

		//Si no decimos lo contrario nos subscribiremos a los monos y flujos de devuelven 
		//los repositorios de R2DBC con el MISMO HILO
		System.out.println("===================================");
		peliculaRepo.findAll().subscribe(x -> System.out.println(x));		
		System.out.println("===================================");
		gestorPeliculas.pruebaTransacciones().block(); // :)
		System.out.println("===================================");
		peliculaRepo.findAll().subscribe(x -> System.out.println(x));		
		
		
				
	}

}
