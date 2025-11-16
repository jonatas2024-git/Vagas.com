// src/main/java/com/example/vagas/model/Perfil.java (Correção Final)

package com.example.vagas.model;

import jakarta.persistence.*;

// O import java.util.UUID não é estritamente necessário nesta classe,
// mas é mantido se você o deixou para referência ao User.id.

@Entity
@Table(name = "perfis")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento One-to-One
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user; // Entidade User

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    private String telefone;
    
    // CAMPOS ADICIONADOS PARA ATUALIZAÇÃO
    private String descricao;
    private String cidade;
    private String estado;
    // ----------------------------------------------------

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

    // --- Getters e Setters Completos ---

    // MÉTODOS BÁSICOS QUE ESTAVAM FALTANDO (Para resolver o erro no DTO)
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
    // FIM DOS MÉTODOS BÁSICOS FALTANTES
    
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
    
    // NOVOS GETTERS E SETTERS (JÁ ESTAVAM CORRETOS)
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    // -----------------------

    public String getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }
}