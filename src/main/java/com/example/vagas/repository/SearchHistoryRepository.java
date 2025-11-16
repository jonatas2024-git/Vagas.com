package com.example.vagas.repository;

import com.example.vagas.model.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, UUID> {

    /**
     * Busca o histórico de buscas por um usuário específico, ordenado por data descendente.
     * Ideal para exibir as últimas buscas do usuário no frontend.
     */
    List<SearchHistory> findByUser_IdOrderBySearchTimeDesc(UUID userId);
    
    // Podemos adicionar métodos para métricas futuras aqui (ex: top 10 termos)
    
}