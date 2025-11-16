package com.example.vagas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Esta anotação fará com que o Spring retorne automaticamente o status HTTP 404
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Construtor que aceita uma mensagem de erro
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Construtor opcional que aceita mensagem e a causa (stack trace)
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}