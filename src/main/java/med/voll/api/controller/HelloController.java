package med.voll.api.controller;
//Uma classe é um Controller

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//Anotação de comunicação de uma API Rest
@RequestMapping("/hello")//Informa qual URL esse controle vai responder que no caso é a /hello enviando diretamente pra mesma

public class HelloController {

    @GetMapping//Usando o GetMapinng  pra que o metodo HTTP chame o método

    public String olaMundo(){
        return  "Olá mundo, Spring Boot :)";
    }
}
