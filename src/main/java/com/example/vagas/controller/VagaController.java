package com.example.vagas.controller;

import com.example.vagas.dto.VagaDTO;
import com.example.vagas.dto.VagaCreateUpdateDTO;
import com.example.vagas.dto.VagaFilterDTO; 
import com.example.vagas.service.VagaService;
//import com.example.vagas.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import java.util.List;

@RestController
@RequestMapping("/api/vagas")
@RequiredArgsConstructor
public class VagaController {

    private final VagaService vagaService;

    // ... (Seus métodos POST, PUT, DELETE e GET por ID)

    // ---------------------------------------------------------------------------------
    // BUSCA AVANÇADA / GERAL (Item 9)
    // ---------------------------------------------------------------------------------
    /**
     * Endpoint unificado para busca de vagas com filtros avançados e paginação.
     * GET /api/vagas/buscar?query=desenvolvedor&minSalary=5000&contractType=CLT&page=0&size=10
     *
     * @param filters DTO que coleta todos os Query Params de filtro.
     * @param pageable Parâmetros de paginação e ordenação (page, size, sort).
     * @return Uma página de VagasDTO.
     */
    @GetMapping("/buscar")
    public ResponseEntity<Page<VagaDTO>> buscarVagasAvancada(
            VagaFilterDTO filters, // O Spring automaticamente mapeia Query Params para este DTO
            Pageable pageable) {   // O Spring injeta automaticamente o objeto Pageable
        
        // Chama o novo método de busca unificado
        Page<VagaDTO> vagasPage = vagaService.searchVagasByFilter(filters, pageable);
        
        return ResponseEntity.ok(vagasPage);
    }
    
    // Exemplo: Listar todas as vagas paginadas (caso você queira manter o endpoint simples)
    @GetMapping
    public ResponseEntity<Page<VagaDTO>> listarTodasVagas(Pageable pageable) {
        Page<VagaDTO> vagas = vagaService.listarTodasVagasPaginadas(pageable);
        return ResponseEntity.ok(vagas);
    }

    // ---------------------------------------------------------------------------------
    // 4. MÉTODOS DE CRIAÇÃO, ATUALIZAÇÃO E DELEÇÃO (Exemplo)
    // ---------------------------------------------------------------------------------

    @PostMapping
    public ResponseEntity<VagaDTO> createVaga(@RequestBody VagaCreateUpdateDTO dto) {
        VagaDTO novaVaga = vagaService.createVaga(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaVaga);
    }

    // ... (o restante dos métodos do Controller)
}