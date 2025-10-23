package com.example.vagas.controller;

import com.example.vagas.model.Perfil;
import com.example.vagas.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// REMOVIDO: import java.util.UUID; // Não está mais em uso.

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    /**
     * Obtém o perfil do usuário atualmente autenticado.
     * Rota: GET /api/perfil/me
     */
    @GetMapping("/me")
    public ResponseEntity<Perfil> getPerfilLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return perfilService.buscarPerfilPorUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Atualiza o perfil do usuário.
     * Rota: PUT /api/perfil
     */
    @PutMapping
    public ResponseEntity<Perfil> updatePerfil(@RequestBody Perfil perfilDetails) {
      
        Perfil updatedPerfil = perfilService.atualizarPerfil(perfilDetails);
        return ResponseEntity.ok(updatedPerfil);
    }
}