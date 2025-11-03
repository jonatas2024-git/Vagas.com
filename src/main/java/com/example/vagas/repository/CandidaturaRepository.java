package com.example.vagas.repository;

import com.example.vagas.model.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {

    // 1. Verificar se o usuário já se candidatou à vaga
    Optional<Candidatura> findByVagaIdAndCandidatoId(Long vagaId, java.util.UUID candidatoId);
    
    // 2. Listar candidaturas de um usuário (Candidato - Meu Painel)
    List<Candidatura> findByCandidatoId(java.util.UUID candidatoId);
    
    // 3. Listar candidaturas para uma vaga (Empresa - Gerenciamento)
    List<Candidatura> findByVagaId(Long vagaId);
}