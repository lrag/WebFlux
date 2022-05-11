package com.curso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.curso.endpoint.proxy.ClienteRestProxy;
import com.curso.modelo.entidad.Cliente;
import com.curso.modelo.entidad.DatosBancarios;

@SpringBootApplication
public class Aplicacion implements CommandLineRunner{

	@Autowired
	private ClienteRestProxy clienteRestProxy;

	public static void main(String[] args) {
		SpringApplication.run(Aplicacion.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("=====================================");
		
		clienteRestProxy
			.listar()
			.subscribe(c-> System.out.println(c));
		
		//Todos los métodos de WebClient tienen el publishOn en otro hilo!!!!
		Thread.sleep(5000);
		
		/*
		System.out.println("=====================================");

		clienteRestProxy
			.buscar("609d64784af9651a96070981")
			.subscribe(c-> System.out.println(c));
		
		Thread.sleep(5000);		
		 */

		System.out.println("=====================================");

		Cliente cliente = new Cliente(null,"Bud Spencer","C/Falsa,123","111222333","1/1/2009", new DatosBancarios("HTb",9999));
		clienteRestProxy
			.insertar(cliente)
			.subscribe(c-> System.out.println(c));
		
		Thread.sleep(5000);				
		
		
		System.out.println("FIN del hilo main");
	}

}
