package com.example.vagas.dto;

import com.example.vagas.model.enums.CandidaturaStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CandidaturaDTO {
    private Long id;
    private Long vagaId;
    private String vagaTitulo;
    private Long empresaId; // Pode ser útil para visualização
    private String empresaNome;
    private java.util.UUID candidatoId; // Opcional, mas útil para Empresa
    private String candidatoNome; // Opcional, mas útil para Empresa
    private CandidaturaStatus status;
    private LocalDateTime dataCandidatura;
}