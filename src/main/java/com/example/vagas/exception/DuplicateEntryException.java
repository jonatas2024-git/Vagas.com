package com.example.vagas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção personalizada para representar uma tentativa de criar um recurso duplicado,
 * como um usuário tentando se candidatar duas vezes à mesma vaga.
 * Mapeia para o código HTTP 409 (Conflict).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEntryException extends RuntimeException {
    
    public DuplicateEntryException(String message) {
        super(message);
    }

    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}