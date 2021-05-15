package com.curso.modelo.persistencia;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.curso.modelo.entidad.Cliente;

//No hace falta @Repository con spring boot
@Repository
public interface ClienteRepository extends ReactiveMongoRepository<Cliente, String>{

}
