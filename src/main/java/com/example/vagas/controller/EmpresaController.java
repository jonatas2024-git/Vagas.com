package com.example.vagas.controller;

import com.example.vagas.service.EmpresaService;
import com.example.vagas.service.VagaMetricsService;
import com.example.vagas.dto.EmpresaCreateUpdateDTO;
import com.example.vagas.model.Empresa;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;
    private final VagaMetricsService vagaMetricsService;

    // CRIAR
    @PostMapping
    public ResponseEntity<Empresa> createEmpresa(@Valid @RequestBody EmpresaCreateUpdateDTO dto) {
        Empresa novaEmpresa = empresaService.createEmpresa(dto);
        return new ResponseEntity<>(novaEmpresa, HttpStatus.CREATED);
    }

    // LISTAR TODAS
    @GetMapping
    public ResponseEntity<?> listAll() {
        return ResponseEntity.ok(empresaService.findAll());
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable Long id) {
        try {
            Empresa empresa = empresaService.findEmpresaById(id);
            return ResponseEntity.ok(empresa);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // MÉTRICAS
    @GetMapping("/{id}/metrics")
    public ResponseEntity<Map<String, Object>> getEmpresaMetrics(@PathVariable Long id) {

        empresaService.verifyOwner(id);

        long totalViews = vagaMetricsService.getTotalViewsForEmpresa(id);
        Map<UUID, Long> topVagasByViews = vagaMetricsService.getTopVagasByViews(id);

        long totalApplications = vagaMetricsService.getTotalApplicationsForEmpresa(id);
        Map<UUID, Long> topVagasByApplications = vagaMetricsService.getTopVagasByApplications(id);

        Map<String, Object> metrics = Map.of(
                "empresaId", id,
                "totalViews", totalViews,
                "topVagasByViews", topVagasByViews,
                "totalApplications", totalApplications,
                "topVagasByApplications", topVagasByApplications
        );

        return ResponseEntity.ok(metrics);
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Empresa> updateEmpresa(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaCreateUpdateDTO dto
    ) {
        try {
            Empresa updated = empresaService.updateEmpresa(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        empresaService.deletarEmpresaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
