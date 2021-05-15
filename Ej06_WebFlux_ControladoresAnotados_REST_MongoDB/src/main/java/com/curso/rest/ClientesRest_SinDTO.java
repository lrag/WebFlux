package com.curso.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.curso.modelo.entidad.Cliente;
import com.curso.modelo.negocio.GestorClientes;
import com.curso.modelo.persistencia.ClienteRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/sinDto/clientes")
public class ClientesRest_SinDTO {

	@Autowired private GestorClientes gestorClientes;
	@Autowired private ClienteRepository clienteRepo;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			     produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Cliente> insertar(@RequestBody Cliente cliente){		
		Mono<Cliente> monoCliente = gestorClientes.insertar(cliente);
		return monoCliente;
	}
	
	@GetMapping(path="/{id}",
			    produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Cliente> buscar(@PathVariable("id") String id){		
		Mono<Cliente> monoCliente = clienteRepo.findById(id);
		return monoCliente;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Cliente> listar(){		
		Flux<Cliente> fluxCliente = clienteRepo.findAll();
		return fluxCliente;
	}
	
}







