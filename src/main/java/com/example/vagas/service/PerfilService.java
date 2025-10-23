package com.example.vagas.service;

import com.example.vagas.model.Perfil;
import com.example.vagas.model.User;
import com.example.vagas.repository.PerfilRepository;
import com.example.vagas.repository.UserRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired 
    private UserRepository userRepository;

    
    public Optional<Perfil> buscarPerfilPorUserId(UUID userId) {
        return perfilRepository.findByUserId(userId);
    }

    public Perfil atualizarPerfil(Perfil perfil) {
        
        return perfilRepository.save(perfil);
    }
    
    // NOVO MÉTODO IMPLEMENTADO
    /**
     * Busca o Perfil associado a um usuário pelo seu username (email).
     * @param username 
     * @return 
     */
    public Optional<Perfil> buscarPerfilPorUsername(String username) {
   
        Optional<User> userOptional = userRepository.findByEmail(username); 

     
        return userOptional.flatMap(user -> {
      
            return perfilRepository.findByUserId(user.getId());
        });
    }
}