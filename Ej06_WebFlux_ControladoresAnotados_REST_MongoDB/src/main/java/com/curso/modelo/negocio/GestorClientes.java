package com.curso.modelo.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.modelo.entidad.Cliente;
import com.curso.modelo.persistencia.ClienteRepository;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

	//si lo dejamos así se borrará cuando el Dispatcher Handler se suscriba al Mono que devolvemos,
	//pero no nos enteraremos de si el cliente existía o no
	//public Mono<Void> borrar_(Cliente cliente){
	//	return clienteRepo.delete(cliente);
	//}	
	
	public Mono<Boolean> borrar(Cliente cliente) {
		return clienteRepo
			.findById(cliente.getId()) //De aqui sale Mono<Cliente> o Mono<Void>
			//.subscribeOn(Schedulers.boundedElastic())
			.flatMap(clienteABorrar -> {
				return clienteRepo
						.delete(clienteABorrar) //Mono<Void>
						.thenReturn(true); //De delete sale un Mono<Void> que sustituimos por un Mono<Boolean> (true)
			})
			.defaultIfEmpty(false);
	}
	
}


