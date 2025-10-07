package com.example.vagas.service;

import com.example.vagas.model.Perfil;
import com.example.vagas.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public Optional<Perfil> buscarPerfilPorUserId(UUID userId) {
        return perfilRepository.findByUserId(userId);
    }

    public Perfil atualizarPerfil(Perfil perfil) {
        // Lógica de validação e salvamento
        return perfilRepository.save(perfil);
    }
}