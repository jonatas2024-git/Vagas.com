// Para Retorno

package com.example.vagas.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VagaDTO {
    private Long id;
    private String titulo;
    
    // Usamos um DTO aninhado ou campos simples para a Empresa, para evitar a entidade JPA.
    private Long empresaId; 
    private String empresaNomeFantasia; 
    
    // Você pode adicionar mais campos da vaga aqui (descrição, salário, etc.)
}