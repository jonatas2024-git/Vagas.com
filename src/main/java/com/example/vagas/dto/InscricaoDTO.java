// src/main/java/com/example/vagas/dto/InscricaoDTO.java

package com.example.vagas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscricaoDTO {
    
    private Long id;

    // Informações da Vaga
    private Long vagaId;
    private String vagaTitulo;

    // Informações do Candidato
    // Necessário para listagens de empresa
    private UUID candidatoId;
    private String candidatoNome; 
    
    private LocalDateTime dataInscricao;
    private String status;
}