package com.curso.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.curso.modelo.entidad.Cliente;
import com.curso.modelo.entidad.ClienteJPA;
import com.curso.modelo.negocio.GestorClientes;
import com.curso.modelo.persistencia.ClienteRepoJPA;
import com.curso.modelo.persistencia.ClienteRepository;
import com.curso.rest.dto.ClienteDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/clientes")
public class ClientesRest {

	@Autowired private GestorClientes gestorClientes;
	@Autowired private ClienteRepository clienteRepo;
	@Autowired private ClienteRepoJPA clienteRepoJPA;
	
	///////////////////////////////////////////////////////////////////
	// Cómo utilizar un api imperativa en una aplicación con WebFlux //
	///////////////////////////////////////////////////////////////////
	@GetMapping("/path")
	public Mono<List<ClienteJPA>> listarNoReactivo(){
		/*Esto funciona, pero es una chapucilla...
		return Mono
				.just("for men")
				//.subscribeOn(Schedulers.boundedElastic())
				.map(s -> {
					return clienteRepoJPA.findAll().stream().map(cliente -> new ClienteDTO(cliente)).collect(Collectors.toList());
				});
		*/

		//...porque existe esto:
		return Mono.fromCallable(() -> {
				return clienteRepoJPA.findAll().stream().collect(Collectors.toList());
			}
		);
	}	

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<List<ClienteDTO>> listar(){
		return clienteRepo
			.findAll() //Devuelve Flux<Cliente>
			.map( cliente -> new ClienteDTO(cliente))
			.collectList();
	}	

	@GetMapping(path="/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ClienteDTO> listarClientesReactivos(){
		return clienteRepo
				.findAll() //Devuelve Flux<Cliente>
				.map( cliente -> new ClienteDTO(cliente) ); //De aqui salen DTOs...
	}	
	
	@GetMapping(path="/{id}", 
			    produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<ClienteDTO>> buscar(@PathVariable("id") String id){	
		System.out.println("BUSCAR:"+id);
		return clienteRepo
			.findById(id) //Devuelve Mono<Cliente> o Mono<Void>!
			.map( cliente -> {
				return new ResponseEntity<ClienteDTO>(new ClienteDTO(cliente), HttpStatus.OK);
			})
			.defaultIfEmpty(new ResponseEntity<ClienteDTO>(HttpStatus.NOT_FOUND));	
	}	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			     produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<ClienteDTO>> insertar(@RequestBody ClienteDTO clienteDto){	
		return gestorClientes
				.insertar(clienteDto.asCliente()) //De aqui sale un Mono<Cliente>
				.map( cliente -> {
					return new ResponseEntity<ClienteDTO>(new ClienteDTO(cliente), HttpStatus.CREATED); //De aqui sale un Mono<ResponseEntity<ClienteDTO>>
				});
	}

	@PutMapping(path="/{id}",
			    consumes = MediaType.APPLICATION_JSON_VALUE, 
			    produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<ClienteDTO>> modificar(@RequestBody ClienteDTO clienteDto){	
		return gestorClientes
			.modificar(clienteDto.asCliente()) //Esto devuelve Mono<Cliente>
			.map( cliente -> {
				return new ResponseEntity<ClienteDTO>(new ClienteDTO(cliente), HttpStatus.OK);//De aqui sale Mono<ResponseEntity>
			});		
	}
	
	@DeleteMapping(path="/{id}")
	public Mono<ResponseEntity<Object>> borrar(@PathVariable("id") String idCliente){
		
		/*
		Cliente c = new Cliente();
		c.setId(idCliente);
		
		return gestorClientes
			.borrar(c) //De aqui sale Mono<Boolean>
			.map( borrado -> {
				if(borrado) {
					return new ResponseEntity<Object>(HttpStatus.OK);
				}
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			});
		*/
		return Mono.just(idCliente) //De aqui nos sacamos de la manga un Mono<String>
			.flatMap( id -> {
				Cliente c = new Cliente();
				c.setId(id);
				return gestorClientes.borrar(c); //De aqui sale un Mono<Boolean>
			})
			.map(borrado -> {
				if(borrado) {
					return new ResponseEntity<Object>(HttpStatus.OK);
				}
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);				
			});
		
	}
	
}









