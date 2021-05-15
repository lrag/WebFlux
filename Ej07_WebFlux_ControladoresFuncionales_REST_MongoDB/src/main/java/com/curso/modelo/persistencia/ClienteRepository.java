package com.curso.modelo.persistencia;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.curso.modelo.entidad.Cliente;

public interface ClienteRepository extends ReactiveMongoRepository<Cliente, String>{

}
