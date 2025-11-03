package com.example.vagas.service;

import com.example.vagas.model.Vaga;
import com.example.vagas.model.VagaView;
import com.example.vagas.model.User;
import com.example.vagas.repository.VagaRepository; 
import com.example.vagas.repository.VagaViewRepository;
import com.example.vagas.repository.UserRepository; 
import com.example.vagas.security.SecurityUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List; // CORRIGIDO: Adicionado import para List

@Service
@RequiredArgsConstructor
public class VagaMetricsService {

    private final VagaRepository vagaRepository;
    private final VagaViewRepository vagaViewRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    // NOTE: ApplicationRepository (ou CandidaturaRepository) será necessário aqui
    // para métricas de candidaturas, mas vamos focar nas views por enquanto.

    /**
     * Registra uma visualização de vaga.
     * @param vagaId O ID da vaga visualizada.
     */
    @Transactional
    public void recordVagaView(Long vagaId) {
        Vaga vaga = vagaRepository.findById(vagaId)
            .orElse(null); // Ignoramos se a vaga não for encontrada (erro 404)

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
    // Métodos para obtenção de Métricas (Item 8)
    // =========================================================================
    
    /**
     * Obtém as contagens de visualização para uma vaga específica.
     */
    public long getViewCountForVaga(Long vagaId) {
        return vagaViewRepository.countByVaga_Id(vagaId);
    }

    /**
     * Obtém o total de visualizações de todas as vagas de uma empresa.
     * @param empresaId ID da Empresa.
     */
    public long getTotalViewsForEmpresa(Long empresaId) {
        return vagaViewRepository.countViewsByEmpresaId(empresaId);
    }
    
    /**
     * Obtém o mapa do ID da Vaga para a Contagem de Views, ordenado do mais visto.
     * @param empresaId ID da Empresa.
     * @return Map<Long, Long> onde Long é o Vaga ID e Long é a contagem de views.
     */
    public Map<Long, Long> getTopVagasByViews(Long empresaId) {
        // Encontra o top N, mas o resultado é uma List<Object[]> (VagaId, ViewCount)
        List<Object[]> results = vagaViewRepository.findTopVagasByViewsForEmpresa(empresaId);

        // Converte o resultado bruto para um Mapa de forma segura
        return results.stream()
            .collect(Collectors.toMap(
                row -> (Long) row[0], // Vaga ID
                row -> (Long) row[1]  // Contagem de Views
            ));
    }
    
    
}