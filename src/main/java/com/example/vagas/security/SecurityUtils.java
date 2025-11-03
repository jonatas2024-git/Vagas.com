package com.example.vagas.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
// NOVO: Import do UserDetails (útil para cenários mistos ou JWT customizado)
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID; // NOVO IMPORT

@Component
public class SecurityUtils {

    /**
     * Tenta obter o ID (UUID) do usuário logado a partir do contexto de segurança.
     * Retorna Optional.empty() se não estiver logado ou se o ID não puder ser extraído.
     */
    public Optional<UUID> getCurrentUserIdOptional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        // 1. Caso de Login Social / JWT (O principal é um JWT)
        if (principal instanceof Jwt jwt) {
            // Assumimos que o ID do usuário (UUID) foi colocado na 'claim' "user_id"
            try {
                // O getClaim pode retornar a String ou o objeto (tentaremos como String e converteremos)
                String userIdString = jwt.getClaimAsString("user_id");
                if (userIdString != null) {
                    return Optional.of(UUID.fromString(userIdString));
                }
            } catch (Exception e) {
                // Se o getClaimAsString falhar ou a conversão falhar, retornamos vazio
                return Optional.empty(); 
            }
        }
        
        // 2. Caso de Principal ser UserDetails (Pode acontecer em testes ou cenários mistos)
        if (principal instanceof UserDetails userDetails) {
            try {
                // Tenta extrair o UUID do username, como no Auth Service padrão
                return Optional.of(UUID.fromString(userDetails.getUsername()));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
    
    /**
     * Retorna o ID (UUID) do usuário logado. Lança exceção se não estiver logado.
     * Usado para ações que exigem autenticação (como ver histórico de buscas ou perfil).
     */
    public UUID getCurrentUserId() { 
        // Usa o método Optional para garantir a segurança e a coerência
        return getCurrentUserIdOptional()
                .orElseThrow(() -> new SecurityException("Usuário não autenticado ou ID do usuário ausente no token."));
    }
}