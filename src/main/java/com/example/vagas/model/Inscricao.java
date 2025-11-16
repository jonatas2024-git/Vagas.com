package com.example.vagas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscricoes")
@Data // Inclui Getters, Setters, toString, equals e hashCode (Lombok)
@NoArgsConstructor // Construtor sem argumentos (para JPA)
@AllArgsConstructor // Construtor com todos os argumentos
@Builder // Padrão Builder para criação simplificada de objetos
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento Many-to-One: Muitos candidatos podem se inscrever em uma vaga
    @ManyToOne(fetch = FetchType.LAZY) // Usando LAZY para performance
    @JoinColumn(name = "vaga_id", nullable = false) // Garantindo que a Vaga é obrigatória
    private Vaga vaga;

    // Relacionamento Many-to-One: Um candidato (User) pode ter muitas inscrições
    // O campo foi renomeado de 'user' para 'candidato' para alinhar com o InscricaoService
    @ManyToOne(fetch = FetchType.LAZY) // Usando LAZY para performance
    @JoinColumn(name = "candidato_id", nullable = false) // Usando 'candidato_id' para clareza semântica
    private User candidato; 

    @Column(name = "data_inscricao", nullable = false)
    private LocalDateTime dataInscricao;

    @Column(name = "status", length = 50)
    private String status; // Ex: PENDENTE, AVALIADO, ACEITO, REJEITADO
}