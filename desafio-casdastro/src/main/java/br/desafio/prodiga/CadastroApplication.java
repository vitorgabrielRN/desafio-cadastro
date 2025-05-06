package br.desafio.prodiga;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CadastroApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CadastroApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("funciona");
	}



}
// URLS
// http://localhost:8080/swagger-ui/index.html
// http://localhost:8080/Cliente/formulario
// http://localhost:8080/Cliente
// isabela barreto 	isabela_barreto@outlook.com 	053.936.702-81 	96159218
// http://localhost:8080/Cliente
