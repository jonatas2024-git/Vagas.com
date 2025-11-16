package com.example.vagas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaCreateUpdateDTO {

    private String nome;
    
    // CORRIGIDO: Adicionado campo nomeFantasia
    private String nomeFantasia; 
    
    private String cnpj;
    private String descricao;
    private String email;
    private String telefone;
}