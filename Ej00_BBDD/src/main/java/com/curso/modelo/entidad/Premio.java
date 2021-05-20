package com.curso.modelo.entidad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Premio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	private String nombre;
	private String fecha;
	@Column(name="FK_ID_PELICULA")
	private Integer idPelicula;
	
	//@ManyToOne
	//private Pelicula pelicula;

	public Premio() {
		super();
	}

	public Premio(Integer id, String nombre, String fecha, Integer idPelicula) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.idPelicula = idPelicula;
	}	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}



	public Integer getIdPelicula() {
		return idPelicula;
	}

	public void setIdPelicula(Integer idPelicula) {
		this.idPelicula = idPelicula;
	}

	@Override
	public String toString() {
		return "Premio [id=" + id + ", nombre=" + nombre + ", fecha=" + fecha + ", idPelicula=" + idPelicula + "]";
	}

}
