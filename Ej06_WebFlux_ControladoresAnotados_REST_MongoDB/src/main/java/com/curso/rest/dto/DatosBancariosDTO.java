package com.curso.rest.dto;

import com.curso.modelo.entidad.DatosBancarios;

public class DatosBancariosDTO {

	private String banco;
	private Integer numeroTC;

	public DatosBancariosDTO() {
		super();
	}

	public DatosBancariosDTO(String banco, Integer numeroTC) {
		super();
		this.banco = banco;
		this.numeroTC = numeroTC;
	}
	
	public DatosBancariosDTO(DatosBancarios datosBancarios) {
		banco = datosBancarios.getBanco();
		numeroTC = datosBancarios.getNumeroTC();
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public Integer getNumeroTC() {
		return numeroTC;
	}

	public void setNumeroTC(Integer numeroTC) {
		this.numeroTC = numeroTC;
	}

	@Override
	public String toString() {
		return "DatosBancarios [banco=" + banco + ", numeroTC=" + numeroTC + "]";
	}
	
	public DatosBancarios asDatosBancarios() {
		return new DatosBancarios(banco, numeroTC);
	}
	
}




