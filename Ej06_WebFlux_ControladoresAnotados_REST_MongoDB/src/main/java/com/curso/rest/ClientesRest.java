package com.curso.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
		Mono<Cliente> monoCliente = gestorClientes.modificar(clienteDto.asCliente());
		return monoCliente.map( cliente -> new ResponseEntity<ClienteDTO>(new ClienteDTO(cliente), HttpStatus.OK));
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<ClienteDTO>> insertar(@RequestBody ClienteDTO clienteDto){	
		Mono<Cliente> monoCliente = gestorClientes.insertar(clienteDto.asCliente());
		return monoCliente.map( cliente -> new ResponseEntity<ClienteDTO>(new ClienteDTO(cliente), HttpStatus.OK));
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
		Flux<Cliente> fluxClientes = clienteRepo.findAll();
		return fluxClientes.map( c -> new ClienteDTO(c));
	}
	
}



