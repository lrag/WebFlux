package com.curso.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.curso.endpoint.ClientesHandler;

import reactor.core.publisher.Mono;

@Configuration
public class ConfiguracionEndpointsFuncionales {

	@Bean
	public RouterFunction<ServerResponse> functionalRoutesClientes(ClientesHandler clientesHandler) {
		return RouterFunctions
			.route(RequestPredicates.GET("/clientes")             , serverRequest -> clientesHandler.listar(serverRequest))
			.andRoute(RequestPredicates.GET("/clientes/{id}")     , serverRequest -> clientesHandler.buscar(serverRequest))
			.andRoute(RequestPredicates.POST("/clientes")         , serverRequest -> clientesHandler.insertar(serverRequest))
			.andRoute(RequestPredicates.PUT("/clientes/{id}")   , serverRequest -> clientesHandler.modificar(serverRequest))
			.andRoute(RequestPredicates.DELETE("/clientes/{id}"), serverRequest -> clientesHandler.borrar(serverRequest));
	}		

	/*
	@Bean
	public RouterFunction<ServerResponse> functionalRoutesFacturas(ClientesHandler clientesHandler) {
		return RouterFunctions
			.route(RequestPredicates.GET("/facturas")             , serverRequest -> facturasHandler.listar(serverRequest))
			.andRoute(RequestPredicates.GET("/facturas/{id}")     , serverRequest -> facturasHandler.buscar(serverRequest))
			.andRoute(RequestPredicates.POST("/facturas")         , serverRequest -> facturasHandler.insertar(serverRequest));
			.andRoute(RequestPredicates.PUT("/facturas/{id}")   , serverRequest -> facturasHandler.insertar(serverRequest));
			.andRoute(RequestPredicates.DELETE("/facturas/{id}"), serverRequest -> facturasHandler.insertar(serverRequest));
	}
	*/		
  
  
  
}
