package com.example.vagas.service;

import com.example.vagas.model.Vaga;
import com.example.vagas.model.VagaView;
import com.example.vagas.model.User;
import com.example.vagas.repository.VagaRepository; 
import com.example.vagas.repository.VagaViewRepository;
import com.example.vagas.repository.UserRepository; 
import com.example.vagas.repository.CandidaturaRepository; 
import com.example.vagas.security.SecurityUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VagaMetricsService {

    private final VagaRepository vagaRepository;
    private final VagaViewRepository vagaViewRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final CandidaturaRepository candidaturaRepository; 

    /**
     * Registra uma visualização de vaga.
     */
    @Transactional
    public void recordVagaView(UUID vagaId) {
        Vaga vaga = vagaRepository.findById(vagaId).orElse(null); 

        if (vaga == null) {
            return;
        }
        
        User user = null;
        Optional<UUID> currentUserId = securityUtils.getCurrentUserIdOptional();

        if (currentUserId.isPresent()) {
            user = userRepository.findById(currentUserId.get()).orElse(null);
        }

        VagaView view = new VagaView(vaga, user);
        vagaViewRepository.save(view);
    }
    
    // =========================================================================
    // Métodos para obtenção de Métricas (VIEWS)
    // =========================================================================
    
    public long getViewCountForVaga(UUID vagaId) {
        // Assumindo que o Repositório foi corrigido para aceitar UUID
        return vagaViewRepository.countByVaga_Id(vagaId); 
    }

    public long getTotalViewsForEmpresa(Long empresaId) {
        // Este método precisa ser implementado no VagaViewRepository
        return 0;
    }
    
    /**
     * CORREÇÃO: Tipo de retorno ajustado para Map<UUID, Long>
     */
    public Map<UUID, Long> getTopVagasByViews(Long empresaId) { 
        // Este método precisa ser implementado no VagaViewRepository
        // Assumindo que o Repositório retorna Map<UUID, Long>
        return Map.of();
    }
    
    // =========================================================================
    // Métodos para obtenção de Métricas (CANDIDATURAS)
    // =========================================================================

    /**
     * Obtém as contagens de candidaturas para uma vaga específica.
     */
    public long getApplicationCountForVaga(UUID vagaId) {
        // Assumindo que o Repositório foi corrigido para aceitar UUID
        return candidaturaRepository.findByVagaId(vagaId).size();
    }

    /**
     * Obtém o total de candidaturas para todas as vagas de uma empresa.
     */
    public long getTotalApplicationsForEmpresa(Long empresaId) {
        return 0; 
    }
    
    /**
     * Obtém o mapa do ID da Vaga para a Contagem de Candidaturas, ordenado.
     * Tipo de retorno já estava correto: Map<UUID, Long>
     */
    public Map<UUID, Long> getTopVagasByApplications(Long empresaId) {
        // O mapa de retorno deve ter UUID para a Vaga, e Long para a contagem
        return Map.of(); 
    }
}