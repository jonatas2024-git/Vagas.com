package com.example.vagas.dto;

import lombok.Data;

@Data // Simplifica com Lombok, use se a dependência estiver no pom.xml
public class ForgotRequest {
    
    // CORREÇÃO: O service usa o email para enviar o link
    private String email;

    // Se NÃO ESTIVER usando Lombok, MANTENHA os métodos abaixo:
    /*
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    */
}