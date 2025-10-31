package com.example.vagas.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import java.util.UUID; // NOVO IMPORT

@Component
public class SecurityUtils {

    /**
     * Obtém o ID do usuário logado a partir do contexto de segurança.
     * @return O ID do usuário logado (UUID).
     */
    public UUID getCurrentUserId() { // CORRIGIDO: Tipo de retorno agora é UUID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Nenhum usuário autenticado no contexto de segurança.");
        }

        Object principal = authentication.getPrincipal();

        // 1. Caso de Login Social / JWT (O principal é um JWT)
        if (principal instanceof Jwt) {
            // Assumimos que o ID do usuário (UUID) foi colocado na 'claim' "user_id"
            // O getClaim pode retornar o objeto diretamente se o tipo for compatível
            UUID userId = ((Jwt) principal).getClaim("user_id"); 
            if (userId != null) {
                return userId;
            }
        } 
        
        // Falhou em extrair o ID de qualquer forma
        throw new IllegalStateException("Não foi possível extrair o ID do usuário (UUID) do token JWT/Principal.");
    }
}