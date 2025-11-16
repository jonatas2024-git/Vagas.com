// src/main/java/com/example/vagas/controller/InscricaoController.java

package com.example.vagas.controller;

import com.example.vagas.dto.InscricaoDTO;
import com.example.vagas.service.InscricaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID; // Importação necessária

@RestController
@RequestMapping("/api/inscricoes")
@RequiredArgsConstructor
public class InscricaoController {

    private final InscricaoService inscricaoService;

    // =========================================================================
    // 1. GESTÃO DO CANDIDATO
    // =========================================================================

    /**
     * POST /api/inscricoes/{vagaId}
     * Permite que o usuário logado se inscreva em uma vaga.
     */
    @PostMapping("/{vagaId}")
    @PreAuthorize("hasRole('CANDIDATO')")
    // CORRIGIDO: Linha 44 (e 89) no log. O ID da Vaga deve ser UUID.
    public ResponseEntity<InscricaoDTO> candidatar(@PathVariable UUID vagaId) { // MUDANÇA: Long para UUID
        InscricaoDTO inscricao = inscricaoService.candidatar(vagaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(inscricao);
    }
    
    /**
     * GET /api/inscricoes/minhas
     * Lista as inscrições do usuário logado.
     */
    @GetMapping("/minhas")
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<List<InscricaoDTO>> listarMinhasInscricoes() {
        return ResponseEntity.ok(inscricaoService.listarMinhasInscricoes());
    }

    /**
     * DELETE /api/inscricoes/{inscricaoId}
     * Cancela a inscrição.
     */
    @DeleteMapping("/{inscricaoId}")
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<Void> cancelarInscricao(@PathVariable Long inscricaoId) { // Inscricao ID é Long, OK
        inscricaoService.cancelarInscricao(inscricaoId);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // 2. GESTÃO DA EMPRESA
    // =========================================================================

    /**
     * GET /api/inscricoes/vagas/{vagaId}/candidatos
     * Lista todos os candidatos para uma vaga. (Apenas o dono da empresa pode acessar)
     */
    @GetMapping("/vagas/{vagaId}/candidatos")
    @PreAuthorize("hasRole('EMPRESA')")
    // CORRIGIDO: Linha 89 (e 44) no log. O ID da Vaga deve ser UUID.
    public ResponseEntity<List<InscricaoDTO>> listarCandidatosPorVaga(@PathVariable UUID vagaId) { // MUDANÇA: Long para UUID
        List<InscricaoDTO> candidatos = inscricaoService.listarCandidatosPorVaga(vagaId);
        return ResponseEntity.ok(candidatos);
    }
}