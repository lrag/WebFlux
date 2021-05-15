package com.curso.modelo.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.modelo.entidad.Cliente;
import com.curso.modelo.persistencia.ClienteRepository;

import reactor.core.publisher.Mono;

@Service
public class GestorClientes {

	@Autowired
	private ClienteRepository clienteRepo;

	public Mono<Cliente> insertar(Cliente cliente) {
		//LN...
		return clienteRepo.save(cliente);
	}
	
	public Mono<Cliente> modificar(Cliente cliente) {
		//LN...
		return clienteRepo.save(cliente);
	}
	
	public Mono<Void> borrar(String idCliente) {
		//LN...
		return clienteRepo.deleteById(idCliente);
	}
	
}


