package com.curso;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;

import com.curso.modelo.entidad.Cliente;
import com.curso.modelo.entidad.DatosBancarios;
import com.curso.modelo.persistencia.ClienteRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@SpringBootApplication

//Excluimos la configuración automática de spring boot relativa a la conexión con mongoDB
//puesto que estamos utilizando un MongoDB empotrado con la aplicación y queremos
//configurarlo expresamente para que mantenga las colecciones entre reinicios de la aplicación
@SpringBootApplication(exclude = EmbeddedMongoAutoConfiguration.class)
public class Aplicacion implements CommandLineRunner{

	@Autowired
	private ClienteRepository clienteRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(Aplicacion.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("===========================================");
		
		Cliente c1 = new Cliente(null,"Bart","C/Evergreen Terrace","555", "ayer",new DatosBancarios("Banco1",1234));
		Cliente c2 = new Cliente(null,"Harry Callahan","S.F.","555","hoy",new DatosBancarios("Banco2",2345));
		Cliente c3 = new Cliente(null,"John McClane","N.Y.","555","mañana",new DatosBancarios("Banco3",3456));
		Cliente c4 = new Cliente(null,"Lisa","C/Evergreen Terrace","555","pasado",new DatosBancarios("Banco4",4567));

		//No te subscribes, no se inserta
		//clienteRepo.save(c1);
		//clienteRepo.save(c2);
		//clienteRepo.save(c3);
		//clienteRepo.save(c4);

		//Nos da igual el resultado, pero se insertan		
		//clienteRepo.save(c1).subscribe();
		//clienteRepo.save(c2).subscribe();
		//clienteRepo.save(c3).subscribe();
		//clienteRepo.save(c4).subscribe();
		

		//Queremos saber algo del resultado
		//clienteRepo.save(c1).subscribe( c -> System.out.println(c));
		//clienteRepo.save(c2).subscribe( c -> System.out.println(c));
		//clienteRepo.save(c3).subscribe( c -> System.out.println(c));
		//clienteRepo.save(c4).subscribe( c -> System.out.println(c));
		
		
		//Nos interesa el orden
		//clienteRepo.save(c1)
		//	.then(clienteRepo.save(c2))
		//	.then(clienteRepo.save(c3))
		//	.then(clienteRepo.save(c4))
		//	.subscribe();
		 

		//Ídem
		//Mono<Cliente> m1 = clienteRepo.save(c1);
		//Mono<Cliente> m2 = clienteRepo.save(c2);
		//Mono<Cliente> m3 = clienteRepo.save(c3);
		//Mono<Cliente> m4 = clienteRepo.save(c4);		
		//Flux.concat(m1,m2,m3,m4).subscribe();
		 
		/*
		clienteRepo
			.deleteAll().thenReturn("OK")
			.flatMap( x -> {
				System.out.println("Borrado");
				return clienteRepo.save(c1);
			})
			.flatMap( rs -> { 
				System.out.println("Insertado c1");
				return clienteRepo.save(c2);
			})
			.flatMap( rs -> { 
				System.out.println("Insertado c2");
				//int n = 10 / 0;
				return clienteRepo.save(c3);
			})
			.flatMap( rs -> { 
				System.out.println("Insertado c3");
				return clienteRepo.save(c4);
			})
			.flatMapMany( rs -> {
				System.out.println("Insertado c4");
				return clienteRepo.findAll();
			})
			.subscribe( c -> System.out.println(c));
		*/
		 
				
	}

}
