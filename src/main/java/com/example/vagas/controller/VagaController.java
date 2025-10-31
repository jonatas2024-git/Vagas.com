// src/main/java/com/example/vagas/controller/VagaController.java

package com.example.vagas.controller;

import com.example.vagas.service.VagaService;
import com.example.vagas.dto.VagaDTO;
import com.example.vagas.dto.VagaCreateUpdateDTO;
import com.example.vagas.exception.ResourceNotFoundException; // NOVO: Para 404
import lombok.RequiredArgsConstructor; // NOVO: Para injeção de construtor
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.data.web.PageableDefault; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // NOVO: Para validação dos DTOs
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600) 
@RestController
@RequestMapping("/api/vagas")
@RequiredArgsConstructor // Injeção de dependência via construtor
public class VagaController {

    private final VagaService vagaService; // Usa injeção de construtor

    // -------------------------------------------------------------------------
    // R (Read) - PÚBLICO
    // -------------------------------------------------------------------------

    /**
     * GET /api/vagas - Retorna lista de VagaDTOs com paginação.
     */
    @GetMapping
    public Page<VagaDTO> getAllVagasPaginadas(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        
        // Retorna Page<VagaDTO> mapeado no Service
        return vagaService.listarTodasVagasPaginadas(pageable);
    }

    /**
     * Rota: GET /api/vagas/buscar?q={termo} - Busca por termo e retorna List<VagaDTO>.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<VagaDTO>> buscarVagas(
            @RequestParam(name = "q") String termoBusca) {
        
        List<VagaDTO> resultados = vagaService.buscarVagasPorTermo(termoBusca);
        
        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(resultados); // 200 OK
    } 

    /**
     * Rota: GET /api/vagas/{id} - Busca vaga por ID e retorna VagaDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VagaDTO> getVagaById(@PathVariable Long id) {
        try {
            VagaDTO vaga = vagaService.buscarPorId(id);
            return ResponseEntity.ok(vaga); // 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // -------------------------------------------------------------------------
    // C (Create) - RESTRITO (Apenas Dono da Empresa)
    // -------------------------------------------------------------------------

    /**
     * POST /api/vagas - Cria nova vaga usando VagaCreateUpdateDTO.
     */
    @PostMapping
    public ResponseEntity<VagaDTO> createVaga(@Valid @RequestBody VagaCreateUpdateDTO dto) {
        try {
            VagaDTO newVaga = vagaService.createVaga(dto);
            return new ResponseEntity<>(newVaga, HttpStatus.CREATED); // 201 Created
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
        } catch (ResourceNotFoundException e) {
            // Se a EmpresaID do DTO não existir
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
    
    // -------------------------------------------------------------------------
    // U (Update) - RESTRITO (Apenas Dono da Empresa)
    // -------------------------------------------------------------------------

    /**
     * PUT /api/vagas/{id} - Atualiza vaga usando VagaCreateUpdateDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VagaDTO> updateVaga(
            @PathVariable Long id, 
            @Valid @RequestBody VagaCreateUpdateDTO dto) {
        try {
            VagaDTO updatedVaga = vagaService.updateVaga(id, dto);
            return ResponseEntity.ok(updatedVaga); // 200 OK
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
    
    // -------------------------------------------------------------------------
    // D (Delete) - RESTRITO (Apenas Dono da Empresa)
    // -------------------------------------------------------------------------

    /**
     * DELETE /api/vagas/{id} 
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaga(@PathVariable Long id) {
        try {
            vagaService.deletarVaga(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}