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

    // Contagem de visualizações por vaga
    long countByVaga_Id(UUID vagaId);

    // Métricas da empresa
    @Query("SELECT count(v) FROM VagaView v WHERE v.vaga.empresa.id = :empresaId")
    long countViewsByEmpresaId(@Param("empresaId") UUID empresaId);

    @Query("SELECT v.vaga.id, count(v) AS viewCount FROM VagaView v " +
           "WHERE v.vaga.empresa.id = :empresaId " +
           "GROUP BY v.vaga.id " +
           "ORDER BY viewCount DESC")
    List<Object[]> findTopVagasByViewsForEmpresa(@Param("empresaId") UUID empresaId);
}
