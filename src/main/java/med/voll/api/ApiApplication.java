package med.voll.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {
	//Essa é a classe que roda os programa se starta todas as dependencias e servidor
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	//spring.application.name=api

}
