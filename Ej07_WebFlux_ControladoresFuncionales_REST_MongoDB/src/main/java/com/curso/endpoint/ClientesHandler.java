package com.curso.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.curso.modelo.entidad.Cliente;
import com.curso.modelo.negocio.GestorClientes;
import com.curso.modelo.persistencia.ClienteRepository;
import com.curso.rest.dto.ClienteDTO;

import reactor.core.publisher.Mono;

@Component
public class ClientesHandler {

	@Autowired private GestorClientes gestorClientes;
	@Autowired private ClienteRepository clienteRepo;
	
	/*
	 
	POST /clientes
	CT: app/json
	-----------------------
	{ cliente }	
	
	*/
	
	public Mono<ServerResponse> insertar(ServerRequest request){		
		return
			request
				.bodyToMono(ClienteDTO.class) //Devuelve Mono<VlienteDTO>
				.map(clienteDTO -> clienteDTO.asCliente())
				.flatMap(cliente -> {
					return ServerResponse
							.ok()
							//.body(gestorClientes.insertar(cliente).flatMap(cli -> Mono.just( new ClienteDTO(cli)) ),
							.body(gestorClientes.insertar(cliente), ClienteDTO.class); 					
				});
	}

	public Mono<ServerResponse> modificar(ServerRequest request){		
		String id = request.pathVariable("id");
		return
			request
				.bodyToMono(ClienteDTO.class) //Devuelve Mono<VlienteDTO>
				.map(clienteDTO -> clienteDTO.asCliente())
				.flatMap(cliente -> {
					return ServerResponse
							.ok()
							//.body(gestorClientes.insertar(cliente).flatMap(cli -> Mono.just( new ClienteDTO(cli)) ),
							.body(gestorClientes.modificar(cliente), ClienteDTO.class); 					
				});
	}
	
	public Mono<ServerResponse> borrar(ServerRequest request){		
		String id = request.pathVariable("id");
		return gestorClientes
					.borrar(id)
					.then(ServerResponse.ok().build());		
	}
	
	
	/*
	GET /clientes/{id}
	
	200 OK
	CT: app/json
	------------------------
	{ cliente }
	
	
	404 NOT FOUND
	CT: app/json
	------------------------
	{ 
		"codigo"  :  404,
		"mensaje" : "No existe 
	}	
	*/
	public Mono<ServerResponse> buscar(ServerRequest request){	
		//request.pathVariable devuelve siempre String
		String id = request.pathVariable("id");		
		
		//return ServerResponse
		//	.ok()
		//	.contentType(MediaType.APPLICATION_JSON)
		//	.body(clienteRepo.findById(id).map( c -> new ClienteDTO(c)), ClienteDTO.class);
		
		return clienteRepo.findById(id) //Devuelve Mono<Cliente> ó Mono<Void>
	            .flatMap( cliente -> {	//Si estamos aquí es que ha sido Mono<Cliente>. Llega CLIENTE y queremos que salga Mono<ServerResponse>           	
	            	return ServerResponse
	            		.ok()
	            		.contentType(MediaType.APPLICATION_JSON)
	            		.body(Mono.just(new ClienteDTO(cliente)), ClienteDTO.class);
	            })
	            .switchIfEmpty(ServerResponse.notFound().build());
	    
	}

	/*
	GET /clientes
	
	200 OK
	CT: app/json
	------------------------------
	[{cliente}]
	
	*/
	public Mono<ServerResponse> listar(ServerRequest request){
		return ServerResponse
			.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(clienteRepo.findAll().map( c -> new ClienteDTO(c)), ClienteDTO.class);	
	}
	
}




