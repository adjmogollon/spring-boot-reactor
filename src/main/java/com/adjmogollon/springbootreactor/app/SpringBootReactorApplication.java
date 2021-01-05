package com.adjmogollon.springbootreactor.app;

import java.util.ArrayList;
import java.util.List;

import com.adjmogollon.springbootreactor.app.models.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Anibal Mogollon");
		usuariosList.add("Lyra Belacqua");
		usuariosList.add("Lyra LenguaPlata");
		usuariosList.add("Iorek Barrison");
		usuariosList.add("Serafina Pekkala");
		usuariosList.add("Lee Scoresby");
		usuariosList.add("Will Parry");

		Flux<String> nombres = Flux.fromIterable(usuariosList);
		
		// Flux<String> nombres = Flux.just("Anibal Mogollon", "Lyra Belacqua", "Lyra
		// LenguaPlata", "Asriel Belacqua", "Iorek Barrison","Serafina Pekkala", "Lee
		// Scoresby", "Will Parry");

		Flux<Usuario> usuarios = nombres
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("Lyra")).doOnNext(usuario -> {
					if (usuario == null) {
						throw new RuntimeException("Nombres no pueden ser vacios");
					}
					System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
				}).map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				});

		usuarios.subscribe(e -> log.info(e.toString()), error -> log.error(error.getMessage()), new Runnable() {

			@Override
			public void run() {
				log.info("Ha finalizado la ejecución del observable con éxito!");
			}
		});
	}

}
