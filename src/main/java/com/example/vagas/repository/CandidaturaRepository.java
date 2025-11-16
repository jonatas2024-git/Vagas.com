package com.example.vagas.repository;

import com.example.vagas.model.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
// CORRIGIDO: O ID da Candidatura é Long, não UUID.
public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> { 

    // Métodos para VagaId e CandidatoId (UUID) estão corretos
    Optional<Candidatura> findByVagaIdAndCandidatoId(UUID vagaId, UUID candidatoId);
    List<Candidatura> findByCandidatoId(UUID candidatoId);
    List<Candidatura> findByVagaId(UUID vagaId);
}