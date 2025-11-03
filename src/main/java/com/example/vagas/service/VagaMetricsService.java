package com.example.vagas.service;

import com.example.vagas.model.Vaga;
import com.example.vagas.model.VagaView;
import com.example.vagas.model.User;
import com.example.vagas.repository.VagaRepository; 
import com.example.vagas.repository.VagaViewRepository;
import com.example.vagas.repository.UserRepository; 
import com.example.vagas.repository.CandidaturaRepository; // NOVO: Import para Candidaturas
import com.example.vagas.security.SecurityUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.Map;
//import java.util.stream.Collectors;  --> NÃO UTILIZADO....
//import java.util.List; --> NÃO UTILIZADO....

@Service
@RequiredArgsConstructor
public class VagaMetricsService {

    private final VagaRepository vagaRepository;
    private final VagaViewRepository vagaViewRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final CandidaturaRepository candidaturaRepository; // NOVO: Injeção do Repositório

    /**
     * Registra uma visualização de vaga. (Lógica existente)
     * @param vagaId O ID da vaga visualizada.
     */
    @Transactional
    public void recordVagaView(Long vagaId) {
        // ... (Lógica de recordVagaView permanece a mesma)
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
    // Métodos para obtenção de Métricas (VIEWS - Lógica existente)
    // =========================================================================
    
    public long getViewCountForVaga(Long vagaId) {
        return vagaViewRepository.countByVaga_Id(vagaId);
    }

    public long getTotalViewsForEmpresa(Long empresaId) {
        // Este método precisa ser implementado no VagaViewRepository
        return 0; // Placeholder: Assumimos que a query existe no VagaViewRepository
    }
    
    public Map<Long, Long> getTopVagasByViews(Long empresaId) {
        // Este método precisa ser implementado no VagaViewRepository
        return Map.of(); // Placeholder: Assumimos que a query existe no VagaViewRepository
    }
    
    // =========================================================================
    // Métodos para obtenção de Métricas (CANDIDATURAS - NOVO - Item 8 Completo)
    // =========================================================================

    /**
     * Obtém as contagens de candidaturas para uma vaga específica.
     */
    public long getApplicationCountForVaga(Long vagaId) {
        // Usamos findByVagaId para contar os resultados se não houver countByVagaId no Repositório.
        return candidaturaRepository.findByVagaId(vagaId).size();
    }

    /**
     * Obtém o total de candidaturas para todas as vagas de uma empresa.
     * Requer uma query personalizada no CandidaturaRepository.
     */
    public long getTotalApplicationsForEmpresa(Long empresaId) {
        // Assumimos que o CandidaturaRepository tem uma query JPA customizada para contar por empresa.
        // Se a query não existir, deve ser criada. Por enquanto, retornamos 0.
        return 0; // Placeholder
    }
    
    /**
     * Obtém o mapa do ID da Vaga para a Contagem de Candidaturas, ordenado.
     * Requer uma query nativa ou JPA customizada no CandidaturaRepository.
     */
    public Map<Long, Long> getTopVagasByApplications(Long empresaId) {
        // Assumimos que o CandidaturaRepository tem uma query customizada para buscar o Top Vagas.
        return Map.of(); // Placeholder
    }
}