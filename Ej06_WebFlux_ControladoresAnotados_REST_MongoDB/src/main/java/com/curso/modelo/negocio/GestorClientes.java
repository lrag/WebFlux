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

	public Mono<Boolean> borrar(Cliente cliente) {
		//LN...
		return clienteRepo
			.findById(cliente.getId()) //De aqui sale Mono<Cliente> o Mono<Void>
			.flatMap(clienteABorrar -> {
				return clienteRepo.delete(clienteABorrar).thenReturn(true); //De aqui sale Mono<Boolean> (true)
			})
			.defaultIfEmpty(false);
	}
	
}


