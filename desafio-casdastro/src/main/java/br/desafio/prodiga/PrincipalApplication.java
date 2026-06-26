package br.desafio.prodiga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAsync
public class PrincipalApplication  {

	public static void main(String[] args) {
		SpringApplication.run(PrincipalApplication.class, args);
		System.out.println("funciona");
	}

	 @Bean 
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

	 }