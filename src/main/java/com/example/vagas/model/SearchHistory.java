package com.example.vagas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "search_history")
@Data
@NoArgsConstructor
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // A busca está ligada ao usuário que a executou (se logado)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Pode ser null se a busca for anônima

    // O termo ou palavra-chave buscada
    @Column(name = "search_term", nullable = false, length = 255)
    private String searchTerm;

    // Campos de filtro (opcional, mas útil para re-executar buscas ou métricas)
    @Column(name = "location")
    private String location; // Ex: "São Paulo, SP"

    @Column(name = "search_time", nullable = false)
    private LocalDateTime searchTime;

    // Construtor para facilitar a criação do registro
    public SearchHistory(User user, String searchTerm, String location) {
        this.user = user;
        this.searchTerm = searchTerm;
        this.location = location;
        this.searchTime = LocalDateTime.now();
    }
}