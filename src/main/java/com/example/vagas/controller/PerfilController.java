package com.example.vagas.controller;

import com.example.vagas.model.Perfil;
import com.example.vagas.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
        
      
        UUID mockedUserId = UUID.fromString("00000000-0000-0000-0000-000000000001"); 
        
        return perfilService.buscarPerfilPorUserId(mockedUserId)
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