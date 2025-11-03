package com.example.vagas.controller;

import com.example.vagas.service.CandidaturaService;
import com.example.vagas.dto.CandidaturaDTO;
import com.example.vagas.dto.CandidaturaStatusUpdateDTO;
import com.example.vagas.exception.ResourceNotFoundException;
import com.example.vagas.exception.DuplicateEntryException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

// Importações do Swagger/OpenAPI 3
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/api/candidaturas")
@RequiredArgsConstructor
@Tag(name = "Candidaturas", description = "Endpoints para candidatos (aplicar) e empresas (gerenciar status).")
public class CandidaturaController {

    private final CandidaturaService candidaturaService;

    // =========================================================================
    // AÇÕES DO CANDIDATO
    // =========================================================================

    @Operation(summary = "Candidatar-se a uma vaga",
               description = "Cria uma nova candidatura para a vaga especificada. Requer JWT de usuário.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Candidatura criada com sucesso.")
    @ApiResponse(responseCode = "404", description = "Vaga ou Usuário não encontrado.")
    @ApiResponse(responseCode = "409", description = "Conflito: Candidatura já existente.")
    @PostMapping("/{vagaId}")
    public ResponseEntity<CandidaturaDTO> applyToVaga(
            @PathVariable Long vagaId) {
        try {
            CandidaturaDTO novaCandidatura = candidaturaService.createCandidatura(vagaId);
            return new ResponseEntity<>(novaCandidatura, HttpStatus.CREATED);
        } catch (DuplicateEntryException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); 
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
    }

    @Operation(summary = "Listar Minhas Candidaturas",
               description = "Retorna todas as candidaturas do usuário logado. Requer JWT de usuário.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Lista de candidaturas retornada.")
    @GetMapping("/me")
    public ResponseEntity<List<CandidaturaDTO>> getMyCandidaturas() {
        List<CandidaturaDTO> candidaturas = candidaturaService.getMyCandidaturas();
        return ResponseEntity.ok(candidaturas);
    }
    
    // =========================================================================
    // AÇÕES DA EMPRESA (DONO DA VAGA)
    // =========================================================================
    
    @Operation(summary = "Listar Candidaturas por Vaga (Empresa)",
               description = "Lista todos os candidatos para uma vaga específica. **Apenas o dono da empresa pode acessar.** Requer JWT.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Lista de candidaturas retornada.")
    @ApiResponse(responseCode = "404", description = "Vaga não encontrada.")
    @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é o dono).")
    @GetMapping("/vaga/{vagaId}")
    public ResponseEntity<List<CandidaturaDTO>> getCandidaturasByVaga(
            @PathVariable Long vagaId) {
        try {
            List<CandidaturaDTO> candidaturas = candidaturaService.getCandidaturasByVagaId(vagaId);
            return ResponseEntity.ok(candidaturas);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    
    @Operation(summary = "Atualizar Status da Candidatura (Empresa)",
               description = "Atualiza o status de uma candidatura (e.g., para 'ENTREVISTA' ou 'REJEITADO'). **Apenas o dono da empresa pode acessar.** Requer JWT.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso.")
    @ApiResponse(responseCode = "404", description = "Candidatura não encontrada.")
    @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é o dono da vaga).")
    @PutMapping("/{candidaturaId}/status")
    public ResponseEntity<CandidaturaDTO> updateCandidaturaStatus(
            @PathVariable Long candidaturaId, 
            @Valid @RequestBody CandidaturaStatusUpdateDTO dto) {
        try {
            CandidaturaDTO updatedCandidatura = candidaturaService.updateCandidaturaStatus(candidaturaId, dto);
            return ResponseEntity.ok(updatedCandidatura);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}