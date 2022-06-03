package com.curso.modelo.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;

import com.curso.modelo.entidad.ClienteJPA;

public interface ClienteRepoJPA extends JpaRepository<ClienteJPA, String>{

}
