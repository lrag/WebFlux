package com.curso.modelo.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

//Table es opcional. Si no se usa dan por sentado que la tabla en la bb.dd. se llama como la clase
//En este caso concreto la tabla será 'PELICULA_HISTORICO'
@Table
public class PeliculaHistorico {

	//Es opcional...hasta que llamemos a findById...
	@Id
	private Integer id;
	private String titulo;
	private String director;
	private String genero;
	private Integer year;
	private String premios;
	
	public PeliculaHistorico() {
		super();
	}
	
	public PeliculaHistorico(Integer id, String titulo, String director, String genero, Integer year, String premios) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.director = director;
		this.genero = genero;
		this.year = year;
		this.premios = premios;
	}

	public PeliculaHistorico(Pelicula pelicula) {
		super();
		this.titulo   = pelicula.getTitulo();
		this.director = pelicula.getDirector();
		this.genero   = pelicula.getGenero();
		this.year     = pelicula.getYear();
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
	
	public String getPremios() {
		return premios;
	}

	public void setPremios(String premios) {
		this.premios = premios;
	}

	@Override
	public String toString() {
		return "PeliculaHistorico [id=" + id + ", titulo=" + titulo + ", director=" + director + ", genero=" + genero
				+ ", year=" + year + ", premios=" + premios + "]";
	}
	
}
