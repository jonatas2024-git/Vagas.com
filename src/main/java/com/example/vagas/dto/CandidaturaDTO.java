package com.example.vagas.dto;

import com.example.vagas.model.enums.CandidaturaStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CandidaturaDTO {
    private Long id;
    private java.util.UUID vagaId; // Correto: UUID
    private String vagaTitulo;
    private Long empresaId; 
    private String empresaNome;
    private java.util.UUID candidatoId; 
    private String candidatoNome;
    private CandidaturaStatus status;
    private LocalDateTime dataCandidatura;
}