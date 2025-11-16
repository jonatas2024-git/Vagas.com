package com.example.vagas.dto;

import lombok.Data; 
import lombok.Builder;
import java.util.UUID; 

@Data 
@Builder // Adicionado para facilitar a construção de objetos com muitos campos
public class AuthResponse {
    private String token;
    private String username;
    private UUID userId; // Adicionado para ser usado na resposta de login

    // NOVO: Campos para o fluxo 2FA
    private boolean isTfaRequired; // Indica se o 2FA está ativo para o usuário
    private boolean isSecondStepRequired; // Indica se o cliente precisa enviar o OTP agora

    // Construtor padrão (necessário por causa do @Data e @Builder juntos)
    public AuthResponse() {}
    
    // Construtor com todos os campos (opcional, mas bom para clareza)
    public AuthResponse(String token, String username, UUID userId, boolean isTfaRequired, boolean isSecondStepRequired) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.isTfaRequired = isTfaRequired;
        this.isSecondStepRequired = isSecondStepRequired;
    }
}