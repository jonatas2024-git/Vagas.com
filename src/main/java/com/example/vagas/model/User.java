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

    // CORREÇÃO: ADICIONAR CAMPO NAME
    @Column(name = "name")
    private String name;

    // CORREÇÃO: ADICIONAR CAMPO EMAIL
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Perfil perfil;

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

    // NOVO: GETTER PARA NAME
    public String getName() {
        return name;
    }
    
    // NOVO: SETTER PARA NAME (Resolve o erro no CustomOAuth2UserService)
    public void setName(String name) {
        this.name = name;
    }

    // CORREÇÃO: ADICIONAR GETTER PARA EMAIL
    public String getEmail() {
        return email;
    }

    // CORREÇÃO: ADICIONAR SETTER PARA EMAIL
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
}