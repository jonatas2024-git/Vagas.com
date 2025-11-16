package com.example.vagas.service;

import com.example.vagas.model.Inscricao;
import com.example.vagas.model.User;
import com.example.vagas.model.Vaga;
import com.example.vagas.repository.InscricaoRepository;
import com.example.vagas.repository.UserRepository;
import com.example.vagas.repository.VagaRepository;
import com.example.vagas.security.SecurityUtils;
import com.example.vagas.dto.InscricaoDTO;
import com.example.vagas.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscricaoService {

    private final InscricaoRepository inscricaoRepository;
    private final VagaRepository vagaRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    // =========================================================================
    // 1. GESTÃO DO CANDIDATO
    // =========================================================================

    /**
     * Permite que o usuário logado se inscreva em uma vaga.
     */
    @Transactional
    public InscricaoDTO candidatar(UUID vagaId) { // CORRIGIDO: Aceita UUID
        // 1. Obter o User logado (Candidato)
        UUID currentUserId = securityUtils.getCurrentUserId();
        User candidato = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário candidato não encontrado."));

        // 2. Obter a Vaga
        Vaga vaga = vagaRepository.findById(vagaId) // ID da Vaga é UUID
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada com ID: " + vagaId));

        // 3. REGRA DE NEGÓCIO: Verificar se o usuário já se candidatou
        // LINHA 52 (no log anterior) corrigida: Usa UUID para Vaga ID.
        if (inscricaoRepository.existsByCandidatoIdAndVagaId(currentUserId, vagaId)) { 
            throw new IllegalArgumentException("Você já está inscrito nesta vaga.");
        }
        
        // 4. Criar e salvar a Inscrição
        Inscricao inscricao = Inscricao.builder()
                .candidato(candidato)
                .vaga(vaga)
                .dataInscricao(LocalDateTime.now())
                .status("PENDENTE") 
                .build();

        return mapToDTO(inscricaoRepository.save(inscricao));
    }

    /**
     * Permite que o usuário cancele sua própria inscrição em uma vaga.
     */
    @Transactional
    public void cancelarInscricao(Long inscricaoId) { // Inscricao ID é Long, OK
        UUID currentUserId = securityUtils.getCurrentUserId();

        Inscricao inscricao = inscricaoRepository.findById(inscricaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Inscrição não encontrada com ID: " + inscricaoId));

        // REGRA DE SEGURANÇA: Apenas o dono da inscrição pode cancelá-la
        if (!inscricao.getCandidato().getId().equals(currentUserId)) {
            throw new SecurityException("Acesso negado. Você não pode cancelar uma inscrição que não é sua.");
        }

        inscricaoRepository.delete(inscricao);
    }

    /**
     * Lista todas as inscrições do usuário logado (Meu Perfil).
     */
    public List<InscricaoDTO> listarMinhasInscricoes() {
        UUID currentUserId = securityUtils.getCurrentUserId();

        return inscricaoRepository.findByCandidatoId(currentUserId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // 2. GESTÃO DA EMPRESA
    // =========================================================================

    /**
     * Lista todos os candidatos para uma vaga específica.
     */
    public List<InscricaoDTO> listarCandidatosPorVaga(UUID vagaId) { // CORRIGIDO: Aceita UUID
        Vaga vaga = vagaRepository.findById(vagaId) // ID da Vaga é UUID
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada com ID: " + vagaId));

        // 1. REGRA DE SEGURANÇA: Checar se o usuário logado é o dono da empresa
        UUID currentUserId = securityUtils.getCurrentUserId();
        
        // LINHA 113 (no log anterior) corrigida: Usa getOwner().getId() que retorna UUID.
        if (!vaga.getEmpresa().getOwner().getId().equals(currentUserId)) {
            throw new SecurityException("Acesso negado. Você não é o dono desta vaga.");
        }

        // 2. Buscar inscrições
        return inscricaoRepository.findByVagaId(vagaId).stream() // ID da Vaga é UUID
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    // =========================================================================
    // 3. MÉTODO DE MAPEAMENTO
    // =========================================================================
    
    private InscricaoDTO mapToDTO(Inscricao inscricao) {
        return InscricaoDTO.builder()
                .id(inscricao.getId()) // Long, OK
                .vagaId(inscricao.getVaga().getId()) // UUID, OK
                .vagaTitulo(inscricao.getVaga().getTitulo())
                .candidatoId(inscricao.getCandidato().getId()) // UUID, OK
                .candidatoNome(inscricao.getCandidato().getUsername())
                .dataInscricao(inscricao.getDataInscricao())
                .status(inscricao.getStatus())
                .build();
    }
}