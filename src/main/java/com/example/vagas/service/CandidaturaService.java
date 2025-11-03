package com.example.vagas.service;

import com.example.vagas.model.Candidatura;
import com.example.vagas.model.Vaga;
import com.example.vagas.model.User;
import com.example.vagas.repository.CandidaturaRepository;
import com.example.vagas.repository.VagaRepository;
import com.example.vagas.repository.UserRepository;
import com.example.vagas.security.SecurityUtils;
import com.example.vagas.dto.CandidaturaDTO;
import com.example.vagas.dto.CandidaturaStatusUpdateDTO;
import com.example.vagas.exception.ResourceNotFoundException;
import com.example.vagas.exception.DuplicateEntryException; // Necessário

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidaturaService {

    private final CandidaturaRepository candidaturaRepository;
    private final VagaRepository vagaRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final EmpresaService empresaService; // Para verificar propriedade da Vaga

    // =========================================================================
    // 1. AÇÕES DO CANDIDATO
    // =========================================================================

    @Transactional
    public CandidaturaDTO createCandidatura(Long vagaId) {
        // 1. Obter o usuário logado (candidato)
        UUID currentUserId = securityUtils.getCurrentUserId();
        User candidato = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário candidato não encontrado."));

        // 2. Validar a Vaga
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada com ID: " + vagaId));
        
        // 3. Verificar Duplicidade
        if (candidaturaRepository.findByVagaIdAndCandidatoId(vagaId, currentUserId).isPresent()) {
            throw new DuplicateEntryException("Você já se candidatou a esta vaga.");
        }

        // 4. Criar e salvar
        Candidatura candidatura = new Candidatura(vaga, candidato);
        
        return mapToDTO(candidaturaRepository.save(candidatura));
    }

    /**
     * Retorna a lista de candidaturas do usuário logado.
     */
    public List<CandidaturaDTO> getMyCandidaturas() {
        UUID currentUserId = securityUtils.getCurrentUserId();
        return candidaturaRepository.findByCandidatoId(currentUserId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // 2. AÇÕES DA EMPRESA (DONO DA VAGA)
    // =========================================================================
    
    /**
     * Lista todas as candidaturas para uma vaga específica.
     * Requer verificação de propriedade.
     */
    public List<CandidaturaDTO> getCandidaturasByVagaId(Long vagaId) {
        // 1. Verifica se a empresa existe E se o usuário logado é o dono da empresa da vaga
        verifyVagaOwner(vagaId); 

        // 2. Retorna a lista
        return candidaturaRepository.findByVagaId(vagaId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza o status de uma candidatura.
     * Requer verificação de propriedade da vaga.
     */
    @Transactional
    public CandidaturaDTO updateCandidaturaStatus(Long candidaturaId, CandidaturaStatusUpdateDTO dto) {
        Candidatura candidatura = candidaturaRepository.findById(candidaturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidatura não encontrada com ID: " + candidaturaId));

        // 1. Verifica se o usuário logado é o dono da empresa dona da vaga
        verifyVagaOwner(candidatura.getVaga().getId());
        
        // 2. Atualiza o status
        candidatura.setStatus(dto.getNewStatus());
        
        return mapToDTO(candidaturaRepository.save(candidatura));
    }

    // =========================================================================
    // 3. AUXILIARES
    // =========================================================================

    /**
     * Verifica se o usuário logado é o dono da empresa dona da vaga.
     */
    private void verifyVagaOwner(Long vagaId) {
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada."));
        
        Long empresaId = vaga.getEmpresa().getId();
        // Reutiliza a lógica de segurança do EmpresaService
        empresaService.verifyOwner(empresaId);
    }

    private CandidaturaDTO mapToDTO(Candidatura c) {
        return CandidaturaDTO.builder()
                .id(c.getId())
                .vagaId(c.getVaga().getId())
                .vagaTitulo(c.getVaga().getTitulo())
                .empresaId(c.getVaga().getEmpresa().getId())
                .empresaNome(c.getVaga().getEmpresa().getNomeFantasia())
                .candidatoId(c.getCandidato().getId())
                .candidatoNome(c.getCandidato().getUsername()) // Assumindo que você usa username ou nome
                .status(c.getStatus())
                .dataCandidatura(c.getDataCandidatura())
                .build();
    }
}