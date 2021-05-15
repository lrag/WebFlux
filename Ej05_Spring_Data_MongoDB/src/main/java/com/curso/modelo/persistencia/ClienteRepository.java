package com.curso.modelo.persistencia;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.curso.modelo.entidad.Cliente;

//@Repository
public interface ClienteRepository extends MongoRepository<Cliente, Integer>{

}
