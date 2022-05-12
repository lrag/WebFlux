package com.curso.modelo.entidad;

public class DatosBancarios {

	private String banco;
	private Integer numeroTC;

	public DatosBancarios() {
		super();
	}

	public DatosBancarios(String banco, Integer numeroTC) {
		super();
		this.banco = banco;
		this.numeroTC = numeroTC;
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
	
}
