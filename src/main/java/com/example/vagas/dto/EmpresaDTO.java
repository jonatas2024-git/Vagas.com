// src/main/java/com/example/vagas/dto/EmpresaDTO.java

package com.example.vagas.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID; // Importação necessária para o ID do dono

@Data
@Builder
public class EmpresaDTO {
    private Long id;
    private String nome;
    private String nomeFantasia;
    
    // CORRIGIDO: Deve ser UUID para corresponder ao User.id
    private UUID ownerUserId; 
}