package com.example.vagas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TfaCodeRequest {
    /**
     * Código OTP (One-Time Password) de 6 dígitos gerado pelo aplicativo Authenticator.
     */
    @NotBlank(message = "O código é obrigatório.")
    @Pattern(regexp = "\\d{6}", message = "O código deve conter 6 dígitos numéricos.")
    private String code;
}