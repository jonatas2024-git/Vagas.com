package com.example.vagas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TfaLoginRequest {
    @NotBlank(message = "O nome de usuário ou email é obrigatório.")
    private String username; 

    @NotBlank(message = "O código OTP é obrigatório.")
    @Pattern(regexp = "\\d{6}", message = "O código OTP deve conter 6 dígitos numéricos.")
    private String code;
}