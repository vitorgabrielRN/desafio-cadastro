package br.desafio.prodiga.desafio.casdastro;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CasdastroApplication implements CommandLineRunner  {

	public static void main(String[] args) { SpringApplication.run(CasdastroApplication.class, args); }
    
	
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("teste");
	}
   
	
	
	

}
