package com.curso;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;

@Component
public class Flujos {
	
	public List<String> listarPalabras(){
		//select * from facturas
		//while
		List<String> lista = new ArrayList<>();
		lista.add("cero");
		lista.add("uno");
		lista.add("dos");
		lista.add("tres");
		lista.add("cuatro");
		lista.add("cinco");
		return lista;		
	}

	//Un flujo para el cual ya tenemos los elementos
	public Flux<String> listarPalabrasFlux() {
		Flux<String> stringFlux = Flux.just("hello","doctor","name","continue","yesterday","tomorrow");
		return stringFlux;
	}	
	
	//Un flujo que emite un elemento cada periodo de tiempo
	public Flux<Long> flux2(){
		Flux<Long> flux = Flux.interval(Duration.ofSeconds(1));
		return flux;
	}

	/*
	//Flujo que indica que el consumidor procesará los elementos utilizando otro hilo
	public Flux<String> flux3() {
		Flux<String> stringFlux = Flux
			.just("hello","doctor","name","continue","yesterday","tomorrow")
			.publishOn(Schedulers.boundedElastic());
		return stringFlux;
	}
	*/
	
	//Este flujo genera mensajes independientes entre si. No tiene que guardar ningun estado
	public Flux<Long> flux6() {
		
		//sink representa al subscriptor
		Flux<Long> flujo = Flux.generate( sink -> {
			Long elemento = Math.round(Math.random()*10000);
			//Sink es el consumidor que el subscriptor ha proporcionado
			//Con 'next' le pasamos el siguiente elemento
			sink.next(elemento);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});		
		return flujo;		
	}	
		
	//En este caso cada mensaje tiene algo que ver con el anterior. Hay que guardar un estado
	public Flux<String> flux4(){
		Flux<String> flux = Flux.generate(
			//El primer parámetro en un Callable que proporciona el estado inicial
		    () -> 1
			, 
			(state, sink) -> {
				sink.next("Mensaje número: "+state);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				/*
				if(state>9) {
					sink.complete();
				} 	
				*/
				//Devolvemos el nuevo estado
				return state+1;
			} 
		);
	
		return flux;
	}
	
	public Flux<List<String>> flux5() throws IOException{
				
		Flux<List<String>> flux = Flux.generate(
			(sink) -> {				
				try {
					WatchKey key = null;
					final Path path = Paths.get("");
					final WatchService watchService = FileSystems.getDefault().newWatchService();
					path.register(watchService, 
					StandardWatchEventKinds.ENTRY_CREATE, 
					StandardWatchEventKinds.ENTRY_DELETE, 
					StandardWatchEventKinds.ENTRY_MODIFY);		

					System.out.println("Esperando...");
					//Esta llamada es bloqueante. Hasta que no haya un cambio en el directorio
					//no pasa a la siguiente línea
					key = watchService.take();
					List<String> mensaje = new ArrayList<>();
					for (WatchEvent<?> event : key.pollEvents()) {
						mensaje.add(event.kind()+":"+event.context());
					}
					sink.next(mensaje);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			} 
		);
		return flux;
	}
	

	
}





/*
Flux.generate(
	() -> {
		Connection cx = DriverManager.getConnection...
		PreparedStatement pst = cx.prepareStatement("select * from facturas")
		ResultSet rs = pst.executeQuery();
		return rs;
	},
	(rs, sink) -> {
	
		Factura f = new Factura()
		f.setCodigo(rs.getString("CODIGO");
		f.setFecha(rs.getDate("FECHA_EMISION");
		f.setTotal(rs.getLong("TOTAL")
		rs.next();
		
		sink.next(f);
		
		return rs;			
	}
) 
*/