// src/main/java/com/example/vagas/model/Empresa.java (Com Relação Owner)

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
    
    @Column(name = "nome_fantasia", nullable = false)
    private String nomeFantasia;

    // NOVO: Relação com o User que é o dono/administrador.
    // Usando ManyToOne, permitindo que um usuário seja dono de várias empresas.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner; 

    @OneToMany(mappedBy = "empresa")
    private List<Vaga> vagas;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    // --- Getters e Setters para o novo campo 'owner' ---
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
    // ----------------------------------------------------

    public List<Vaga> getVagas() {
        return vagas;
    }

    public void setVagas(List<Vaga> vagas) {
        this.vagas = vagas;
    }
}