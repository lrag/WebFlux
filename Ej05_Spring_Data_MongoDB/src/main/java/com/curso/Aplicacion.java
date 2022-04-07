package com.curso;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;

import com.curso.modelo.entidad.Cliente;
import com.curso.modelo.persistencia.ClienteRepository;

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
		
		//clienteRepo.deleteAll();
		
		Cliente c1 = new Cliente(null,"Bart","C/Evergreen Terrace","666");
		Cliente c2 = new Cliente(null,"Harry Callahan","S.F.","555");
		
		clienteRepo.save(c1);
		clienteRepo.save(c2);

		List<Cliente> clientes = clienteRepo.findAll();
		clientes.forEach(c -> System.out.println(c));
		
		System.out.println("===========================================");
		
	}

}
