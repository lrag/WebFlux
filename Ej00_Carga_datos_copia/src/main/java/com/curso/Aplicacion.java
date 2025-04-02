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
	
		pr.deleteAll();
		pr.save(new Pelicula(null,"2001","Stanley Kubrik","Ci-fi","2000"));
		pr.save(new Pelicula(null,"Alien","Ridley Scott","Ci-fi","2000"));
		pr.save(new Pelicula(null,"Die Hard","John McTiernan","Acción","2000"));
		pr.save(new Pelicula(null,"Young Frankenstein","Mel Brooks","Comedia","2000"));
		pr.save(new Pelicula(null,"Los violentos de Kelly","Brian G. Hutton","Bélica","2000"));
		pr.save(new Pelicula(null,"La diligencia","John Ford","Western","2000"));
		pr.save(new Pelicula(null,"Depredador","John McTiernan","Acción","2000"));
		pr.save(new Pelicula(null,"Rio Bravo","Howard Hawks","Western","2000"));
		pr.save(new Pelicula(null,"Harry el sucio","Don Siegel","Policiaca","1971"));
		pr.save(new Pelicula(null,"Desafio Total","Paul Verhoeven","Ci-fi","1990"));	    		
		pr.save(new Pelicula(null,"Tiburón","Steven Spielberg","Acción","1975"));	    		
		pr.save(new Pelicula(null,"Aterriza como puedas","David Zucker, Jerry Zucker, Jim Abrahams","Comedia","1980"));
		pr.save(new Pelicula(null,"Terminator","James Cameron","Ci-Fi","1984"));
		pr.save(new Pelicula(null,"Akira","Katsuhiro Ōtomo","Ci-Fi","1988"));
		pr.save(new Pelicula(null,"El último grán héroe","John McTiernan","Acción","1993"));
		pr.save(new Pelicula(null,"El resplandor","Stanley Kubrik","Terror","1980"));		
		
	}

}
