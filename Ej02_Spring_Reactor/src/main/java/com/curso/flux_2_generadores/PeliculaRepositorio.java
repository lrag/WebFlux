package com.curso.flux_2_generadores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.curso.modelo.entidad.Pelicula;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class PeliculaRepositorio {

	@Autowired
	private DataSource dataSource;
	
	public List<Pelicula> findAll(){	
		List<Pelicula> peliculas = new ArrayList<>();
		try (Connection cx = dataSource.getConnection()) {
			PreparedStatement pst = cx.prepareStatement("select * from pelicula");
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				Pelicula p = new Pelicula(
						rs.getInt("ID"),
						rs.getString("TITULO"),
						rs.getString("DIRECTOR"),
						rs.getString("GENERO"),
						rs.getInt("YEAR")
					);
				peliculas.add(p);	
				Thread.sleep(500);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		return peliculas;
	}
	
	public Flux<Pelicula> findAll_Reactivo(){		
		return Flux.generate(
				//State supplier
				() -> {
					Connection cx = dataSource.getConnection();
					PreparedStatement pst = cx.prepareStatement("select * from pelicula");
					pst.setFetchSize(20);
					ResultSet rs = pst.executeQuery();
					return rs;					
				},
				//Generator
				(rs, consumidores) -> {
					try {
						if(rs.next()) {
							Pelicula p = new Pelicula(
									rs.getInt("ID"),
									rs.getString("TITULO"),
									rs.getString("DIRECTOR"),
									rs.getString("GENERO"),
									rs.getInt("YEAR")
								);
							Thread.sleep(500);
							consumidores.next(p);
						} else {
							consumidores.complete();
						}
					} catch (Exception e) {
						e.printStackTrace();
						consumidores.error(e);
					}
					return rs; 
				},
				//State consumer
				//Se invoca a modo de 'finaly', después de que el generator haya invocado 'complete'
				rs -> {
					System.out.println("Cerrando la conexión");
					try {
						rs.getStatement().getConnection().close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}			
			);		
	}
	
	//La manera correcta de utilizar un api sincrono/bloqueante en una app reactiva es esta:
	public Mono<List<Pelicula>> findAll_Reactivo_Sin_Historias(){		
		return Mono.create(
				//Generator
				(consumidores) -> {
					List<Pelicula> peliculas = new ArrayList<>();
					try (Connection cx = dataSource.getConnection()) {
						PreparedStatement pst = cx.prepareStatement("select * from pelicula");
						ResultSet rs = pst.executeQuery();
						while(rs.next()) {
							Pelicula p = new Pelicula(
									rs.getInt("ID"),
									rs.getString("TITULO"),
									rs.getString("DIRECTOR"),
									rs.getString("GENERO"),
									rs.getInt("YEAR")
								);
							peliculas.add(p);	
						}
						consumidores.success(peliculas);
					} catch (SQLException e) {
						e.printStackTrace();
						consumidores.error(e);
					} 					
				});		
	}
	
	
	public Mono<Pelicula> findById(Integer idPelicula) {
		
		/*
		return Mono.fromRunnable(() -> {
			//CODIGO CON I/O BLOQUEANTE
			//O QUE TARDA EN EJECUTARSE
		});
		*/
		
		return Mono.create(consumidores -> {			
			try(Connection cx = dataSource.getConnection()) {
				PreparedStatement pst = cx.prepareStatement("select * from pelicula where id=?");
				pst.setInt(1, idPelicula);
				ResultSet rs = pst.executeQuery();
				
				if(rs.next()) {
					Pelicula p = new Pelicula(
							rs.getInt("ID"),
							rs.getString("TITULO"),
							rs.getString("DIRECTOR"),
							rs.getString("GENERO"),
							rs.getInt("YEAR")
						);						
					consumidores.success(p);	
				} else {
					Exception e = new Exception("No existe una pelicula con ese id");
					consumidores.error(e);
				}
			} catch (SQLException e) {				
				consumidores.error(e);
			}	
		});	
	}	
	
}
