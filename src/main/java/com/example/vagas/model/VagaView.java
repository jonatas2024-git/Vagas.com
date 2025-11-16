package com.example.vagas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vaga_view")
@Data
@NoArgsConstructor
public class VagaView {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Vaga que foi visualizada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga; 

    // Usuário que visualizou (pode ser null se for anônimo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Quando a visualização ocorreu
    @Column(name = "view_time", nullable = false)
    private LocalDateTime viewTime;

    // Construtor para registro
    public VagaView(Vaga vaga, User user) {
        this.vaga = vaga;
        this.user = user;
        this.viewTime = LocalDateTime.now();
    }
}