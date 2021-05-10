package com.curso.modelo.negocio;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
//<bean id="servicioTareasAnotaciones" class="..." scope="singleton"/>
public class ServicioTareasAnotaciones {

	//El planificador solo invoca métodos que no reciben
	//parametros e ignora el valor devuelto
	@Scheduled(fixedRate=5000)
	public void metodo1(){
		System.out.println(Thread.currentThread().getId()+":Metodo 1 anotaciones");
	}
	
	@Scheduled(fixedDelay=5000)
	public void metodo2(){
		System.out.println(Thread.currentThread().getId()+":Metodo 2 anotaciones");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getId()+":Metodo 2 Fin anotaciones");
	}
	
	@Scheduled(initialDelay=5000, fixedRate=1000)
	public void metodo3(){
		System.out.println(Thread.currentThread().getId()+":Metodo 3 anotaciones");
	}
	
}
