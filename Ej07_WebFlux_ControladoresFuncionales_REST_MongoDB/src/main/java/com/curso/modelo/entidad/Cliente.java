package com.curso.modelo.entidad;

import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Cliente {

	@Id
	private String id;
	@NotBlank
	private String nombre;
	@NotBlank
	private String direccion;
	@NotBlank
	private String telefono;
	@NotBlank
	private String fechaAlta;

	
	private DatosBancarios datosBancarios;

	public Cliente() {
		super();
	}

	public Cliente(String id, @NotBlank String nombre, @NotBlank String direccion, @NotBlank String telefono,
			       @NotBlank String fechaAlta, DatosBancarios datosBancarios) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.fechaAlta = fechaAlta;
		this.datosBancarios = datosBancarios;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public DatosBancarios getDatosBancarios() {
		return datosBancarios;
	}

	public void setDatosBancarios(DatosBancarios datosBancarios) {
		this.datosBancarios = datosBancarios;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" + telefono
				+ ", fechaAlta=" + fechaAlta + ", datosBancarios=" + datosBancarios + "]";
	}

}
