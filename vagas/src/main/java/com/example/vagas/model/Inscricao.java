package com.example.vagas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inscricoes")
public class Inscricao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "vaga_id")
    private Vaga vaga;
    // outros campos
    // getters e setters
}
