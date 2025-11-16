// Para Recebimento/Requisição

package com.example.vagas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VagaCreateUpdateDTO {
    
    @NotBlank(message = "O título da vaga é obrigatório.")
    private String titulo;
    
    @NotNull(message = "A vaga deve estar associada a uma empresa.")
    private Long empresaId; 
    
    // Você deve adicionar todos os campos da Vaga que serão atualizados (descrição, salário, etc.).
}