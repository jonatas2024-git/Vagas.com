// src/main/java/com/example/vagas/controller/CandidaturaController.java

package com.example.vagas.controller;

import com.example.vagas.dto.CandidaturaDTO;
import com.example.vagas.dto.CandidaturaStatusUpdateDTO;
import com.example.vagas.service.CandidaturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID; // Importação necessária

@RestController
@RequestMapping("/api/candidaturas")
@RequiredArgsConstructor
public class CandidaturaController {

    private final CandidaturaService candidaturaService;

    // =========================================================================
    // 1. AÇÕES DO CANDIDATO
    // =========================================================================

    /**
     * POST /api/candidaturas/{vagaId}
     * O Candidato se inscreve em uma Vaga.
     */
    @PostMapping("/{vagaId}")
    @PreAuthorize("hasRole('CANDIDATO')")
    // CORRIGIDO: Linha 46 (e 79) no log. O ID da Vaga deve ser UUID.
    public ResponseEntity<CandidaturaDTO> candidatar(
        @PathVariable UUID vagaId // MUDANÇA: Long para UUID
    ) {
        CandidaturaDTO dto = candidaturaService.createCandidatura(vagaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * GET /api/candidaturas/minhas
     * Lista as candidaturas do usuário logado.
     */
    @GetMapping("/minhas")
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<List<CandidaturaDTO>> getMinhasCandidaturas() {
        return ResponseEntity.ok(candidaturaService.getMyCandidaturas());
    }

    // =========================================================================
    // 2. AÇÕES DA EMPRESA (DONO DA VAGA)
    // =========================================================================

    /**
     * GET /api/candidaturas/vagas/{vagaId}
     * Lista todos os candidatos para uma vaga específica. (Apenas o dono da empresa pode acessar)
     */
    @GetMapping("/vagas/{vagaId}")
    @PreAuthorize("hasRole('EMPRESA')")
    // CORRIGIDO: Linha 79 (e 46) no log. O ID da Vaga deve ser UUID.
    public ResponseEntity<List<CandidaturaDTO>> getCandidatosPorVaga(
        @PathVariable UUID vagaId // MUDANÇA: Long para UUID
    ) {
        List<CandidaturaDTO> candidatos = candidaturaService.getCandidaturasByVagaId(vagaId);
        return ResponseEntity.ok(candidatos);
    }

    /**
     * PUT /api/candidaturas/{candidaturaId}/status
     * Atualiza o status de uma candidatura. (Apenas o dono da empresa pode alterar)
     */
    @PutMapping("/{candidaturaId}/status")
    @PreAuthorize("hasRole('EMPRESA')")
    public ResponseEntity<CandidaturaDTO> updateStatus(
        @PathVariable Long candidaturaId, // ID da Candidatura é Long, OK.
        @RequestBody CandidaturaStatusUpdateDTO dto
    ) {
        CandidaturaDTO updatedDto = candidaturaService.updateCandidaturaStatus(candidaturaId, dto);
        return ResponseEntity.ok(updatedDto);
    }
}