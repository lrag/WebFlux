package com.curso.modelo.negocio;

public class Tarea implements Runnable {

	private String nombre;

	public Tarea(String nombre) {
		super();
		this.nombre = nombre;
	}
	
	public void run(){
		
		System.out.println(Thread.currentThread().getId()+":Tarea "+nombre+" iniciandose");
		long inicio = System.currentTimeMillis();
		while(System.currentTimeMillis()<inicio+1000){
		}
		System.out.println(Thread.currentThread().getId()+":Tarea "+nombre+" fin");
		
	}
	
	
}
