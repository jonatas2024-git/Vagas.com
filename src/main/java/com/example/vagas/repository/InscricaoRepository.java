package com.example.vagas.repository;

import com.example.vagas.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID; // Necessário para o ID do Candidato (User)

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    /**
     * Busca uma inscrição específica pelo ID do candidato (UUID) e ID da vaga (Long).
     * Usado para verificar a existência de uma inscrição.
     */
    Optional<Inscricao> findByCandidatoIdAndVagaId(UUID candidatoId, Long vagaId);

    /**
     * Lista todas as inscrições feitas por um candidato específico.
     * Usado para a função "Minhas Inscrições".
     */
    List<Inscricao> findByCandidatoId(UUID candidatoId);

    /**
     * Lista todos os candidatos inscritos em uma vaga específica.
     * Usado pelo dono da empresa/vaga para ver a lista de candidatos.
     */
    List<Inscricao> findByVagaId(Long vagaId);

    /**
     * Verifica se uma inscrição específica já existe para um dado candidato e vaga.
     * Usado no InscricaoService para evitar candidaturas duplicadas.
     */
    boolean existsByCandidatoIdAndVagaId(UUID candidatoId, Long vagaId);
}