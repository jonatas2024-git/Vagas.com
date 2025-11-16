package com.example.vagas.dto;

import com.example.vagas.model.enums.CandidaturaStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CandidaturaStatusUpdateDTO {
    
    @NotNull(message = "O novo status não pode ser nulo.")
    private CandidaturaStatus newStatus;
}