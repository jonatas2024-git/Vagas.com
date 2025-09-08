package com.example.vagas.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "vagas")
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    // outros campos
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    @OneToMany(mappedBy = "vaga")
    private List<Inscricao> inscricoes;
    // getters e setters
}
