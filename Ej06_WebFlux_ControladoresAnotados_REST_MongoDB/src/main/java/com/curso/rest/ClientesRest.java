package com.curso.rest;

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
import com.curso.modelo.negocio.GestorClientes;
import com.curso.modelo.persistencia.ClienteRepository;
import com.curso.rest.dto.ClienteDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/clientes")
public class ClientesRest {

	@Autowired private GestorClientes gestorClientes;
	@Autowired private ClienteRepository clienteRepo;
	
	@PutMapping(path="/{id}",
			    consumes = MediaType.APPLICATION_JSON_VALUE, 
			    produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<ClienteDTO>> modificar(@RequestBody ClienteDTO clienteDto){	
		return gestorClientes
			.insertar(clienteDto.asCliente())
			.map( cliente -> {
				return new ResponseEntity<ClienteDTO>(new ClienteDTO(cliente), HttpStatus.OK);
			});		
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<ClienteDTO>> insertar(@RequestBody ClienteDTO clienteDto){	
		return gestorClientes
			.insertar(clienteDto.asCliente())
			.map( cliente -> {
				return new ResponseEntity<ClienteDTO>(new ClienteDTO(cliente), HttpStatus.CREATED);
			});
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

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<ClienteDTO> listar(){
		return clienteRepo
			.findAll() //Aqui llegan clientes
			.map( cliente -> new ClienteDTO(cliente) );
	}
	
	@DeleteMapping(path="/{id}")
	public Mono<ResponseEntity<Object>> borrar(@PathVariable("id") String idCliente){
		/*
		Cliente c = new Cliente();
		c.setId(id);
		return gestorClientes
				.borrar(c);
		*/

		return Mono
			.just(idCliente)
			.map( id -> {
				Cliente c = new Cliente();
				c.setId(id);
				return c;
			})
			.flatMap( cliente -> {
				return gestorClientes.borrar(cliente);
			})
			.thenReturn(new ResponseEntity<Object>(HttpStatus.OK));	
		
	}
	
}



