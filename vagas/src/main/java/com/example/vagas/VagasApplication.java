package com.example.vagas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.vagas") // Adicione esta linha
public class VagasApplication {

    public static void main(String[] args) {
        SpringApplication.run(VagasApplication.class, args);
    }
}