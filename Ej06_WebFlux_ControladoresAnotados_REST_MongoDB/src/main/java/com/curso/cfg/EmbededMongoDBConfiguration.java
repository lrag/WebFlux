package com.curso.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbededMongoDBConfiguration {

	@Bean
	public MongoInMemory mongoInMemory(@Value("${spring.data.mongodb.port:27017}") int port,
			                           @Value("${spring.data.mongodb.host:localhost}") String host) {

		//Esta bean se encarga de arrancar el MongoDB empotrado (si lo hace spring boot borra el esquema
		//al detener la aplicacion)
		return new MongoInMemory(port, host);
	}

}
