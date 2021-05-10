package com.curso.modelo.negocio;

//Esta clase tendría logica de negocio
public class ServicioTareas {

	//El planificador solo invoca métodos que no reciben
	//parametros e ignora el valor devuelto
	public void metodo1(){
		System.out.println(Thread.currentThread().getId()+":Metodo 1");
	}
	
	public void metodo2(){
		System.out.println(Thread.currentThread().getId()+":Metodo 2");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getId()+":Metodo 2 Fin");
	}
	
	public void metodo3(){
		System.out.println(Thread.currentThread().getId()+":Metodo 3");
	}

	
	
	
}
