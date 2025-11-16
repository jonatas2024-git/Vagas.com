package com.example.vagas.dto;

import lombok.Data; // Usando Lombok para getters/setters/construtor

// Importações para validação (Ex: jakarta.validation ou javax.validation)
// import javax.validation.constraints.Size; 

@Data // Gera Getters, Setters, toString, hashCode, equals
public class PerfilUpdateDTO {

    // CAMPO FALTANTE ADICIONADO (Resolve o erro no PerfilService.java)
    private String nomeCompleto; 

    // Adicione anotações de validação, se necessário
    private String descricao;
    private String telefone;
    private String cidade;
    private String estado;
    // ... adicione todos os campos que o usuário pode atualizar
}