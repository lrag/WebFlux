package com.curso.flux_1;

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
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
public class Flujos {

	public List<String> listarPalabras(){
		List<String> palabras = Arrays.asList("HELLO", "DOCTOR", "NAME", "CONTINUE", "YESTERDAY", "TOMORROW");
		return palabras;
	}
	
	//Un flujo para el cual ya tenemos los elementos
	public Flux<String> listarPalabras_Reactivo() {
		Flux<String> stringFlux = Flux.just("HELLO", "DOCTOR", "NAME", "CONTINUE", "YESTERDAY", "TOMORROW");
		return stringFlux;
	}
	
}
