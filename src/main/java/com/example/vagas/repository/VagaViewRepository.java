package com.example.vagas.repository;

import com.example.vagas.model.VagaView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VagaViewRepository extends JpaRepository<VagaView, UUID> {

    // Método para obter a contagem de visualizações por Vaga
    long countByVaga_Id(Long vagaId);
    
    // =========================================================================
    // Métricas para a Empresa (para o painel de métricas que criaremos)
    // =========================================================================
    
    /**
     * Conta o total de visualizações para todas as vagas de uma determinada empresa.
     */
    @Query("SELECT count(v) FROM VagaView v WHERE v.vaga.empresa.id = :empresaId")
    long countViewsByEmpresaId(@Param("empresaId") Long empresaId);
    
    /**
     * Lista o top N de vagas por contagem de visualizações em uma determinada empresa.
     * Esta é a chave para o Item 8.
     */
    @Query("SELECT v.vaga.id, count(v) AS viewCount FROM VagaView v " +
           "WHERE v.vaga.empresa.id = :empresaId " +
           "GROUP BY v.vaga.id " +
           "ORDER BY viewCount DESC")
    List<Object[]> findTopVagasByViewsForEmpresa(@Param("empresaId") Long empresaId);
}