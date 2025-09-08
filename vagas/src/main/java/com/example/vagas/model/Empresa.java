package com.example.vagas.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "empresas")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    // outros campos
    @OneToMany(mappedBy = "empresa")
    private List<Vaga> vagas;
    // getters e setters
}
