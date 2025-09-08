package com.example.vagas.model;

import jakarta.persistence.*;

@Entity 
@Table(name = "users")
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    // ... Outros atributos como nome, email, etc.

    // Getters e Setters
    // ...
}