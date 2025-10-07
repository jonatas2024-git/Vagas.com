package com.example.vagas.dto;

import lombok.Data; 

@Data 
public class AuthResponse {
    private String token;
    private String username;
   
    public AuthResponse() {}
}