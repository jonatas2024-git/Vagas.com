package com.example.vagas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                 // <-- VOLTAR PARA Long (original do projeto)

    private String nome;
    private String nomeFantasia;     // campo que o projeto espera

    private String cnpj;
    private String descricao;
    private String email;
    private String telefone;

    // RELAÇÃO COM USUÁRIO (owner) - assume que User.id é UUID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;
}
