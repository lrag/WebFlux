package com.curso.endpoint.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.curso.modelo.entidad.Cliente;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ClienteRestProxy {

	//Mejor que nos inyecten la historia esta...
	//private WebClient webClient = WebClient.create("http://localhost:8080");

	@Autowired
	private WebClient webClientClientes;
	
	
	public Flux<Cliente> listar() {		
		return webClientClientes
				.get()
				.uri("/clientes")
				.retrieve()
				.bodyToFlux(Cliente.class);
		
	}

	public Mono<Cliente> buscar(String idCliente) {		
		return webClientClientes
				.get()
				.uri("/clientes/"+idCliente)
				.retrieve()
				.bodyToMono(Cliente.class);
		
	}

	public Mono<Cliente> insertar(Cliente cliente) {
	    return webClientClientes
	    	.post()
	        .uri("/clientes")
	        .body(Mono.just(cliente), Cliente.class)
	        .retrieve()
	        .bodyToMono(Cliente.class);
	}	
	
}
