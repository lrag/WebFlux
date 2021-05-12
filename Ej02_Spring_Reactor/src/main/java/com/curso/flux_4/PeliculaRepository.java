package com.curso.flux_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.curso.modelo.entidad.Pelicula;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PeliculaRepository {

	@Autowired
	private DataSource dataSource;
	
	public Flux<Pelicula> findAll(){
		
		Flux<Pelicula> flux = Flux.generate(
				//State supplier
				() -> {
					Connection cx = dataSource.getConnection();
					PreparedStatement pst = cx.prepareStatement("select * from pelicula");
					pst.setFetchSize(1);
					ResultSet rs = pst.executeQuery();
					return rs;
				},
				//Generator
				(rs, sink) -> {	
					try {
						if(rs.next()) {
							Pelicula p = new Pelicula(
									rs.getInt("ID"),
									rs.getString("TITULO"),
									rs.getString("DIRECTOR"),
									rs.getString("GENERO"),
									rs.getInt("YEAR")
								);
							sink.next(p);
							Thread.sleep(500);
						} else {
							sink.complete();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}					
					return rs;
				},
				//State consumer
				(rs) -> {
					System.out.println("Cerrando la conexión");
					try {
						rs.getStatement().getConnection().close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}				
			);		
		return flux;
	}
	
	public Mono<Pelicula> findById(Integer id){		
		Mono<Pelicula> mono = Mono.create(
				sink -> {	
					try(Connection cx = dataSource.getConnection()){
						PreparedStatement pst = cx.prepareStatement("select * from pelicula where id=?");
						pst.setInt(1, id);
						ResultSet rs = pst.executeQuery();
						if(rs.next()) {
							Pelicula p = new Pelicula(
									rs.getInt("ID"),
									rs.getString("TITULO"),
									rs.getString("DIRECTOR"),
									rs.getString("GENERO"),
									rs.getInt("YEAR")
								);
							sink.success(p);
						} 
					} catch (SQLException e) {
						e.printStackTrace();
					} 
				}
			);		
		return mono;
	}	
	
	
}
