package com.example.vagas.dto;

import lombok.Data; 

@Data 
public class ResetRequest {
    
    private String token;
    private String newPassword;
    
    // Não inclua os métodos get/set explicitamente. O Lombok faz isso para você!
}