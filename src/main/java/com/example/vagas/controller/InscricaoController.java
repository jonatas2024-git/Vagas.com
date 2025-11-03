package com.example.vagas.controller;

import com.example.vagas.service.InscricaoService;
import com.example.vagas.dto.InscricaoDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Importações do Swagger/OpenAPI 3
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequestMapping("/api/inscricoes")
@RequiredArgsConstructor
@Tag(name = "Inscrições", description = "Endpoints para candidatos se inscreverem em vagas e para empresas visualizarem candidatos.")
public class InscricaoController {

    private final InscricaoService inscricaoService;

    // =========================================================================
    // 1. ENDPOINTS DO CANDIDATO
    // =========================================================================

    @Operation(summary = "Candidatar-se a uma vaga", 
               description = "Permite que o usuário logado faça uma inscrição em uma vaga específica. Requer JWT e ID da Vaga.",
               tags = {"Candidato"})
    @ApiResponse(responseCode = "201", description = "Inscrição criada com sucesso.")
    @ApiResponse(responseCode = "400", description = "Usuário já inscrito na vaga.")
    @ApiResponse(responseCode = "404", description = "Vaga não encontrada.")
    @ApiResponse(responseCode = "401", description = "Não autenticado (JWT ausente/inválido).")
    @PostMapping("/{vagaId}")
    public ResponseEntity<InscricaoDTO> candidatar(
        @Parameter(description = "ID da Vaga para a qual o candidato deseja se inscrever")
        @PathVariable Long vagaId
    ) {
        InscricaoDTO newInscricao = inscricaoService.candidatar(vagaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newInscricao);
    }

    @Operation(summary = "Listar minhas Inscrições", 
               description = "Retorna todas as inscrições que o usuário logado possui.",
               tags = {"Candidato"})
    @ApiResponse(responseCode = "200", description = "Lista de inscrições retornada com sucesso.")
    @ApiResponse(responseCode = "401", description = "Não autenticado.")
    @GetMapping("/minhas")
    public ResponseEntity<List<InscricaoDTO>> listarMinhasInscricoes() {
        List<InscricaoDTO> inscricoes = inscricaoService.listarMinhasInscricoes();
        return ResponseEntity.ok(inscricoes);
    }
    
    @Operation(summary = "Cancelar Inscrição", 
               description = "Cancela uma inscrição existente. **Apenas o candidato dono da inscrição pode cancelá-la.**",
               tags = {"Candidato"})
    @ApiResponse(responseCode = "204", description = "Inscrição cancelada com sucesso.")
    @ApiResponse(responseCode = "404", description = "Inscrição não encontrada.")
    @ApiResponse(responseCode = "403", description = "Acesso negado (inscrição não pertence ao usuário logado).")
    @DeleteMapping("/{inscricaoId}")
    public ResponseEntity<Void> cancelarInscricao(
        @Parameter(description = "ID da Inscrição a ser cancelada")
        @PathVariable Long inscricaoId
    ) {
        inscricaoService.cancelarInscricao(inscricaoId);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // 2. ENDPOINTS DA EMPRESA (Para listar candidatos em uma vaga específica)
    // =========================================================================

    @Operation(summary = "Listar Candidatos por Vaga", 
               description = "Lista todos os candidatos inscritos em uma vaga. **Apenas o dono da empresa que criou a vaga tem acesso.**",
               tags = {"Empresa"})
    @ApiResponse(responseCode = "200", description = "Lista de candidatos retornada com sucesso.")
    @ApiResponse(responseCode = "404", description = "Vaga não encontrada.")
    @ApiResponse(responseCode = "403", description = "Acesso negado (usuário logado não é o dono da vaga).")
    @GetMapping("/vagas/{vagaId}/candidatos")
    public ResponseEntity<List<InscricaoDTO>> listarCandidatosPorVaga(
        @Parameter(description = "ID da Vaga para listar os candidatos")
        @PathVariable Long vagaId
    ) {
        List<InscricaoDTO> candidatos = inscricaoService.listarCandidatosPorVaga(vagaId);
        return ResponseEntity.ok(candidatos);
    }
}