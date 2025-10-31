// src/main/java/com/example/vagas/dto/EmpresaCreateUpdateDTO.java

package com.example.vagas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmpresaCreateUpdateDTO {
    
    @NotBlank(message = "O nome da empresa é obrigatório.")
    private String nome;
    
    @NotBlank(message = "O nome fantasia é obrigatório.")
    private String nomeFantasia;
    
    // Adicione validações para outros campos
}