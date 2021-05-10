package com.curso.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;

@Configuration
@EnableR2dbcRepositories
public class Configuracion {

    @Bean
    public H2ConnectionFactory connectionFactory() {
        return new H2ConnectionFactory(
            H2ConnectionConfiguration.builder()
              .file("c:/h2/bbdd_webflux")
              .username("sa")
              .password("")
              .build()
        );
    }	
	
}
