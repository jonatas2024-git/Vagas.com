// src/main/java/com/example/vagas/dto/PerfilDTO.java

package com.example.vagas.dto;

import com.example.vagas.model.Perfil;
import lombok.Builder; 
import lombok.Data;

@Data
@Builder
public class PerfilDTO {

    private Long id; // ID da entidade Perfil
    private String nomeCompleto; // CORRIGIDO: Deve ser 'nomeCompleto'
    private String email; 
    private String telefone;
    
    // CAMPOS ADICIONADOS NA ENTIDADE (SE EXISTIREM) ou removidos se não forem usados.
    private String descricao;
    private String cidade;
    private String estado;
    // ------------------------------------------------------------------------

    /**
     * Mapeia a entidade Perfil para o DTO.
     * @param perfil A entidade Perfil.
     * @return PerfilDTO.
     */
    public static PerfilDTO fromEntity(Perfil perfil) {
        // Obter o email do objeto User relacionado
        String userEmail = perfil.getUser() != null ? perfil.getUser().getEmail() : null;
        
        return PerfilDTO.builder()
                .id(perfil.getId())
                .nomeCompleto(perfil.getNomeCompleto()) // CORRIGIDO: Chamando getNomeCompleto()
                .email(userEmail) 
                .telefone(perfil.getTelefone())

                // ATENÇÃO: Estes campos não existem em Perfil.java. Mapeá-los para null por enquanto.
                // OU, se você for adicionar, pule para a seção 2.
                .descricao(null)
                .cidade(null)
                .estado(null)
                
                .build();
    }
}