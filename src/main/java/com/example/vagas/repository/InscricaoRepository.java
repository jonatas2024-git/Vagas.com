package com.example.vagas.repository;

import com.example.vagas.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
// CORRIGIDO: O ID da Inscrição é Long, não UUID.
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> { 

    // Todos os métodos de busca estão corretos (usando UUID para candidatoId e vagaId)
    Optional<Inscricao> findByCandidatoIdAndVagaId(UUID candidatoId, UUID vagaId);
    List<Inscricao> findByCandidatoId(UUID candidatoId);
    List<Inscricao> findByVagaId(UUID vagaId);
    boolean existsByCandidatoIdAndVagaId(UUID candidatoId, UUID vagaId);
}