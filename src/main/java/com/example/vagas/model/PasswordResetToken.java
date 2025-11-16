package com.example.vagas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data 
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id 
    private String token; 

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // Construtores
    public PasswordResetToken() {}

    public PasswordResetToken(String token, User user, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    
}