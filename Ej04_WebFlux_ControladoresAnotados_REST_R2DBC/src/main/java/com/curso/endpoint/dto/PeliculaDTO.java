package com.curso.endpoint.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.curso.modelo.entidad.Pelicula;

@Table
public class PeliculaDTO {

	@Id
	private Integer id;
	private String titulo;
	private String director;
	private String genero;
	private Integer year;
	
	public PeliculaDTO() {
		super();
	}

	public PeliculaDTO(Integer id, String titulo, String director, String genero, Integer year) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.director = director;
		this.genero = genero;
		this.year = year;
	}
	
	public PeliculaDTO(Pelicula pelicula) {
		id       = pelicula.getId();
		titulo   = pelicula.getTitulo();
		director = pelicula.getDirector();
		genero   = pelicula.getGenero();
		year     = pelicula.getYear();		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "Pelicula [id=" + id + ", titulo=" + titulo + ", director=" + director + ", genero=" + genero + ", year="
				+ year + "]";
	}
	
	public Pelicula getAsPelicula() {
		return new Pelicula(id, titulo, director, genero, year);
	}

}
