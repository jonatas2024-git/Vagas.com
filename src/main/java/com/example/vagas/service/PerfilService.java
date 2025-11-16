package com.example.vagas.service;

import com.example.vagas.model.Perfil;
import com.example.vagas.model.User; 
import com.example.vagas.repository.PerfilRepository;
import com.example.vagas.repository.UserRepository; 
import com.example.vagas.security.SecurityUtils; 
import com.example.vagas.exception.ResourceNotFoundException; 
import com.example.vagas.dto.PerfilUpdateDTO; 
import com.example.vagas.dto.PerfilDTO; 

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID; // Necessário pois o ID do usuário agora é UUID

@Service
@RequiredArgsConstructor 
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final UserRepository userRepository; 
    private final SecurityUtils securityUtils; // OBRIGATÓRIO

    // =========================================================================
    // 1. GESTÃO DO PERFIL DO USUÁRIO LOGADO (Segura)
    // =========================================================================

    /**
     * Obtém o perfil do usuário logado, usando o ID (UUID) extraído do JWT/Contexto.
     * @return PerfilDTO.
     */
    public PerfilDTO getPerfilDoUsuarioLogado() {
        // 1. Obtém o ID do usuário logado (UUID)
        UUID userId = securityUtils.getCurrentUserId(); 

        // 2. Busca o Perfil associado a esse ID (findByUserId agora aceita UUID)
        Perfil perfil = perfilRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado para o ID do usuário: " + userId));

        // 3. Retorna o DTO
        return PerfilDTO.fromEntity(perfil); 
    }

    /**
     * Atualiza o perfil do usuário logado.
     * @param updateDTO Dados de atualização recebidos do cliente.
     * @return PerfilDTO.
     */
    @Transactional
    public PerfilDTO updatePerfilDoUsuarioLogado(PerfilUpdateDTO updateDTO) {
        // 1. Obtém o ID do usuário logado
        UUID userId = securityUtils.getCurrentUserId();

        // 2. Busca o perfil original
        Perfil perfil = perfilRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado para o ID do usuário: " + userId));

        // 3. Aplica as mudanças do DTO para a entidade
        
        // setDescricao agora existe na entidade Perfil.java
        if (updateDTO.getDescricao() != null) {
            perfil.setDescricao(updateDTO.getDescricao());
        }
        
        if (updateDTO.getTelefone() != null) {
            perfil.setTelefone(updateDTO.getTelefone());
        }
        
        // NOVOS CAMPOS ADICIONADOS NA ENTIDADE:
        if (updateDTO.getCidade() != null) {
            perfil.setCidade(updateDTO.getCidade());
        }
        if (updateDTO.getEstado() != null) {
            perfil.setEstado(updateDTO.getEstado());
        }
        if (updateDTO.getNomeCompleto() != null) {
            perfil.setNomeCompleto(updateDTO.getNomeCompleto());
        }
        
        // 4. Salva e retorna o DTO atualizado
        Perfil updatedPerfil = perfilRepository.save(perfil);
        return PerfilDTO.fromEntity(updatedPerfil);
    }
    
    // =========================================================================
    // MÉTODOS ORIGINAIS (CORRIGIDOS PARA USAR UUID)
    // =========================================================================

    // Corrigido para o tipo UUID
    public Optional<Perfil> buscarPerfilPorUserId(UUID userId) { 
        return perfilRepository.findByUserId(userId);
    }

    public Perfil atualizarPerfil(Perfil perfil) {
        return perfilRepository.save(perfil);
    }
    
    // Corrigido para buscar o ID do User (UUID)
    public Optional<Perfil> buscarPerfilPorUsername(String username) {
   
        Optional<User> userOptional = userRepository.findByEmail(username); 

     
        return userOptional.flatMap(user -> {
            // user.getId() retorna UUID, que agora é o tipo esperado pelo perfilRepository.findByUserId()
            return perfilRepository.findByUserId(user.getId());
        });
    }
}