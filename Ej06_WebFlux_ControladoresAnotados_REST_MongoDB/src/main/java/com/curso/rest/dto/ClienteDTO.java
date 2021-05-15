package com.curso.rest.dto;

import com.curso.modelo.entidad.Cliente;

public class ClienteDTO {

	private String id;
	private String nombre;
	private String direccion;
	private String telefono;
	private String fechaAlta;
	private DatosBancariosDTO datosBancarios;

	public ClienteDTO() {
		super();
	}

	public ClienteDTO(String id, String nombre, String direccion, String telefono, String fechaAlta, DatosBancariosDTO datosBancarios) {
		super();
		this.id             = id;
		this.nombre         = nombre;
		this.direccion      = direccion;
		this.telefono       = telefono;
		this.fechaAlta      = fechaAlta;
		this.datosBancarios = datosBancarios;
	}
	
	public ClienteDTO(Cliente cliente) {
		this.id             = cliente.getId();
		this.nombre         = cliente.getNombre();
		this.direccion      = cliente.getDireccion();
		this.telefono       = cliente.getTelefono();
		this.fechaAlta      = cliente.getFechaAlta();
		this.datosBancarios = new DatosBancariosDTO(cliente.getDatosBancarios());
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

	public DatosBancariosDTO getDatosBancarios() {
		return datosBancarios;
	}

	public void setDatosBancarios(DatosBancariosDTO datosBancarios) {
		this.datosBancarios = datosBancarios;
	}

	public Cliente asCliente() {
		return new Cliente(id,nombre,direccion,telefono,fechaAlta,datosBancarios.asDatosBancarios());
	}
	
}
