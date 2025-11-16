package com.example.vagas.model;

import com.example.vagas.model.enums.CandidaturaStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
// Garante que o usuário não possa se candidatar à mesma vaga duas vezes
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"vaga_id", "candidato_id"})) 
public class Candidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com a Vaga
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    // Relacionamento com o Usuário (Candidato)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidato_id", nullable = false)
    private User candidato;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CandidaturaStatus status = CandidaturaStatus.ENVIADA; // Status inicial

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCandidatura = LocalDateTime.now();
    
    // Construtor conveniente
    public Candidatura(Vaga vaga, User candidato) {
        this.vaga = vaga;
        this.candidato = candidato;
    }
}