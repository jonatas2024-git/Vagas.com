package com.example.vagas.model;

import jakarta.persistence.*;
import java.util.UUID; 

@Entity
@Table(name = "users") 
public class User {

    @Id 
    private UUID id; 

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    // =========================================================================
    // NOVO: CAMPOS PARA 2FA (Two-Factor Authentication)
    // =========================================================================
    @Column(name = "tfa_secret")
    private String tfaSecret;

    @Column(name = "is_tfa_enabled", nullable = false)
    private boolean isTfaEnabled = false; // Começa desabilitado

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Perfil perfil;

    // =========================================================================
    // Getters e Setters Existentes
    // =========================================================================
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Perfil getPerfil() {
        return perfil;
    }
    
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    // =========================================================================
    // NOVO: Getters e Setters para 2FA
    // =========================================================================

    public String getTfaSecret() {
        return tfaSecret;
    }

    public void setTfaSecret(String tfaSecret) {
        this.tfaSecret = tfaSecret;
    }

    public boolean isTfaEnabled() {
        return isTfaEnabled;
    }

    public void setTfaEnabled(boolean isTfaEnabled) {
        this.isTfaEnabled = isTfaEnabled;
    }
}