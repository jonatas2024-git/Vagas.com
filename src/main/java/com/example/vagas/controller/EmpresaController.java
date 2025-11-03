package com.example.vagas.controller;

import com.example.vagas.service.EmpresaService;
// NOVO: Importando o VagaMetricsService
import com.example.vagas.service.VagaMetricsService; 
import com.example.vagas.dto.EmpresaDTO;
import com.example.vagas.dto.EmpresaCreateUpdateDTO;
import com.example.vagas.exception.ResourceNotFoundException; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List; 
// NOVO: Import para Map e Long
import java.util.Map;
import java.lang.Long;

// Importações do Swagger/OpenAPI 3
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor 
@Tag(name = "Empresas", description = "Endpoints para gerenciamento de Empresas por usuários autenticados.")
public class EmpresaController {

    private final EmpresaService empresaService; 
    // NOVO: Injeção do serviço de Métricas
    private final VagaMetricsService vagaMetricsService; 


    @Operation(summary = "Criar nova Empresa", 
               description = "Cria uma nova empresa, vinculando-a ao usuário logado (dono). Requer JWT.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Empresa criada com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição.")
    @ApiResponse(responseCode = "401", description = "Não autenticado.")
    @PostMapping 
    public ResponseEntity<EmpresaDTO> createEmpresa(@Valid @RequestBody EmpresaCreateUpdateDTO dto) {
        EmpresaDTO newEmpresa = empresaService.createEmpresa(dto);
        return new ResponseEntity<>(newEmpresa, HttpStatus.CREATED); 
    }

    @Operation(summary = "Listar todas as Empresas (Público)",
               description = "Retorna uma lista de todas as empresas cadastradas no sistema. Acesso público.")
    @ApiResponse(responseCode = "200", description = "Lista de empresas retornada com sucesso.")
    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> getAllEmpresas() {
        List<EmpresaDTO> empresas = empresaService.findAllEmpresas(); 
        return ResponseEntity.ok(empresas); 
    }
    
    @Operation(summary = "Buscar Empresa por ID (Público)",
               description = "Retorna os detalhes de uma empresa específica pelo seu ID. Acesso público.")
    @ApiResponse(responseCode = "200", description = "Empresa encontrada com sucesso.")
    @ApiResponse(responseCode = "404", description = "Empresa não encontrada.")
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> getEmpresaById(
            @Parameter(description = "ID da Empresa")
            @PathVariable Long id) {
        try {
            EmpresaDTO empresa = empresaService.findEmpresaById(id); 
            return ResponseEntity.ok(empresa);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================================================================
    // ENDPOINT DE MÉTRICAS (Item 8 COMPLETO)
    // =========================================================================
    
    @Operation(summary = "Obter Métricas de Engajamento da Empresa (Restrito)",
               description = "Retorna métricas de visualização (views) e **candidaturas (applications)** das vagas da empresa. **Apenas o dono da empresa pode acessar.** Requer JWT.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Métricas retornadas com sucesso.")
    @ApiResponse(responseCode = "404", description = "Empresa não encontrada.")
    @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é o dono da empresa).")
    @GetMapping("/{id}/metrics")
    public ResponseEntity<Map<String, Object>> getEmpresaMetrics(
            @Parameter(description = "ID da Empresa para obter métricas")
            @PathVariable Long id) {
        
        try {
            // 1. Verificação de Segurança (Garantir que o usuário logado é o dono)
            empresaService.verifyOwner(id); 

            // 2. Coletar Métricas de Views
            long totalViews = vagaMetricsService.getTotalViewsForEmpresa(id);
            Map<Long, Long> topVagasByViews = vagaMetricsService.getTopVagasByViews(id);

            // 3. Coletar Métricas de Candidaturas <-- CORREÇÃO/ADICIONAL
            long totalApplications = vagaMetricsService.getTotalApplicationsForEmpresa(id);
            Map<Long, Long> topVagasByApplications = vagaMetricsService.getTopVagasByApplications(id);

            // 4. Montar o DTO/Mapa de Resposta
            Map<String, Object> metrics = Map.of(
                "empresaId", id,
                // Views
                "totalViews", totalViews,
                "topVagasByViews", topVagasByViews,
                // Candidaturas
                "totalApplications", totalApplications, 
                "topVagasByApplications", topVagasByApplications
            );

            return ResponseEntity.ok(metrics);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
    }

    // =========================================================================
    // ENDPOINTS DE MANIPULAÇÃO (UPDATE e DELETE)
    // =========================================================================

    @Operation(summary = "Atualizar Empresa (Restrito)",
               description = "Atualiza os dados de uma empresa. **Apenas o usuário criador da empresa (dono) pode atualizar.** Requer JWT.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso.")
    @ApiResponse(responseCode = "404", description = "Empresa não encontrada.")
    @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é o dono da empresa).")
    @PutMapping("/{id}") 
    public ResponseEntity<EmpresaDTO> updateEmpresa(
            @Parameter(description = "ID da Empresa a ser atualizada")
            @PathVariable Long id, 
            @Valid @RequestBody EmpresaCreateUpdateDTO dto) {
        try {
            EmpresaDTO updatedEmpresa = empresaService.updateEmpresa(id, dto);
            return ResponseEntity.ok(updatedEmpresa);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
    }

    @Operation(summary = "Deletar Empresa (Restrito)",
               description = "Deleta uma empresa. **Apenas o usuário criador da empresa (dono) pode deletar.** Requer JWT.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Empresa deletada com sucesso (Sem Conteúdo).")
    @ApiResponse(responseCode = "404", description = "Empresa não encontrada.")
    @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é o dono da empresa).")
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> deleteEmpresa(
            @Parameter(description = "ID da Empresa a ser deletada")
            @PathVariable Long id) {
        try {
            empresaService.deletarEmpresaPorId(id);
            return ResponseEntity.noContent().build(); 
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
    }
}