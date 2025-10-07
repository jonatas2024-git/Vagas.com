package com.example.vagas.model;

import jakarta.persistence.*;
// O import de java.util.UUID não é necessário nesta classe

@Entity
@Table(name = "perfis")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento One-to-One: Esta entidade 'Perfil' possui a coluna de FK.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    private String telefone;
    
    // Campo para requisitos como 'tema' ou 'notificações'
    private String preferencias; 

    // Construtor padrão (necessário pelo JPA/Hibernate)
    public Perfil() {
    }

    // Construtor útil para criação inicial (usado no AuthService)
    public Perfil(User user, String nomeCompleto) {
        this.user = user;
        this.nomeCompleto = nomeCompleto;
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }
}