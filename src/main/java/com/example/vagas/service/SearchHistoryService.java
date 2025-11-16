package com.example.vagas.service;

import com.example.vagas.model.SearchHistory;
import com.example.vagas.model.User;
import com.example.vagas.repository.SearchHistoryRepository;
import com.example.vagas.repository.UserRepository; // Para buscar o User logado

import com.example.vagas.security.SecurityUtils; // NOVO: Assumindo que você tem um SecurityUtils
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils; // Ferramenta para obter o usuário logado

    /**
     * Registra uma busca. Se o usuário estiver logado, vincula a ele.
     * @param searchTerm O termo principal buscado (ex: "Desenvolvedor Java")
     * @param location O filtro de localização (ex: "São Paulo")
     */
    @Transactional
    public void recordSearch(String searchTerm, String location) {
        Optional<UUID> currentUserId = securityUtils.getCurrentUserIdOptional();
        User user = null;
        
        if (currentUserId.isPresent()) {
            // Busca o objeto User para vinculação (Melhor que usar a referência direto)
            user = userRepository.findById(currentUserId.get()).orElse(null);
        }

        // Não registra se o termo de busca principal for vazio ou nulo
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return;
        }

        SearchHistory history = new SearchHistory(user, searchTerm.trim(), location);
        searchHistoryRepository.save(history);
    }
    
    /**
     * Obtém o histórico de buscas do usuário logado.
     */
    public List<SearchHistory> getMySearchHistory() {
        UUID userId = securityUtils.getCurrentUserId(); // Assume que o usuário deve estar logado para ver o histórico
        
        return searchHistoryRepository.findByUser_IdOrderBySearchTimeDesc(userId);
    }
}