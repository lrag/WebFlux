package com.curso.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Configuracion {

	@Bean
	public WebClient webClientClientes(WebClient.Builder webClientBuilder) {
		return webClientBuilder.baseUrl("http://localhost:8080").build();
	}
	
}
