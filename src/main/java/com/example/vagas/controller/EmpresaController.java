// src/main/java/com/example/vagas/controller/EmpresaController.java

package com.example.vagas.controller;

import com.example.vagas.service.EmpresaService;
import com.example.vagas.dto.EmpresaDTO;
import com.example.vagas.dto.EmpresaCreateUpdateDTO;
import com.example.vagas.exception.ResourceNotFoundException; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List; // O List agora será usado nos métodos GET

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor 
public class EmpresaController {

    private final EmpresaService empresaService; 

    @PostMapping // Criação de Empresa - Apenas para USUÁRIOS LOGADOS
    public ResponseEntity<EmpresaDTO> createEmpresa(@Valid @RequestBody EmpresaCreateUpdateDTO dto) {
        EmpresaDTO newEmpresa = empresaService.createEmpresa(dto);
        return new ResponseEntity<>(newEmpresa, HttpStatus.CREATED); // Retorna 201 Created
    }

    /**
     * Rota: GET /api/empresas - Lista todas as empresas (Acesso público)
     * ATENÇÃO: Se a lista for muito grande, considere adicionar paginação aqui.
     */
    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> getAllEmpresas() {
        // O Service deve implementar findAll e mapear o resultado para List<EmpresaDTO>
        List<EmpresaDTO> empresas = empresaService.findAllEmpresas(); 
        return ResponseEntity.ok(empresas); // Retorna 200 OK
    }
    
    /**
     * Rota: GET /api/empresas/{id} - Busca empresa por ID (Acesso público)
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> getEmpresaById(@PathVariable Long id) {
        try {
            // O Service deve implementar a busca e o mapeamento
            EmpresaDTO empresa = empresaService.findEmpresaById(id); 
            return ResponseEntity.ok(empresa);
        } catch (ResourceNotFoundException e) {
            // Usa a exceção para retornar 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}") // Atualização - Apenas para o DONO
    public ResponseEntity<EmpresaDTO> updateEmpresa(
            @PathVariable Long id, 
            @Valid @RequestBody EmpresaCreateUpdateDTO dto) {
        try {
            EmpresaDTO updatedEmpresa = empresaService.updateEmpresa(id, dto);
            return ResponseEntity.ok(updatedEmpresa);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Proibido (Não é o dono)
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Não encontrado
        }
    }

    @DeleteMapping("/{id}") // Deleção - Apenas para o DONO
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        try {
            empresaService.deletarEmpresaPorId(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Proibido (Não é o dono)
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Não encontrado
        }
    }
}