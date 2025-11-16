// src/main/java/com/example/vagas/dto/EmpresaDTO.java

package com.example.vagas.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID; 

@Data
@Builder
public class EmpresaDTO {
    // CORRIGIDO: ID da Empresa padronizado para Long
    private Long id; 
    private String nome;
    private String nomeFantasia;
    
    // O ID do Dono (User) é UUID, OK.
    private UUID ownerUserId; 
}