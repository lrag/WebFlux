package com.curso.flux;

import java.util.function.Consumer;

import org.springframework.stereotype.Component;

@Component
public class Consumidor implements Consumer<String>{

	@Override
	public void accept(String texto) {
		System.out.println("Consumidor bean: "+texto);
	}

}
