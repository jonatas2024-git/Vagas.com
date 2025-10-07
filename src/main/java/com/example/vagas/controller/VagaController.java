package com.example.vagas.controller;

import com.example.vagas.model.Vaga;
import com.example.vagas.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.data.web.PageableDefault; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600) 
@RestController
@RequestMapping("/api/vagas")
public class VagaController {

    @Autowired
    private VagaService vagaService;

    /**
     * GET /api/vagas 
     */
    @GetMapping
    public Page<Vaga> getAllVagasPaginadas(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        
        return vagaService.listarTodasVagasPaginadas(pageable);
    }

    /**
     * NOVO ENDPOINT DE BUSCA AVANÇADA
     * Rota: GET /api/vagas/buscar?q={termo}
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Vaga>> buscarVagas(
            @RequestParam(name = "q") String termoBusca) {
        
        List<Vaga> resultados = vagaService.buscarVagasPorTermo(termoBusca);
        
        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        } // CORREÇÃO: Chave de fechamento do 'if' estava faltando.

        return ResponseEntity.ok(resultados); 
    } // CORREÇÃO: Chave de fechamento do método estava faltando ou desbalanceada.

    @GetMapping("/{id}")
    public ResponseEntity<Vaga> getVagaById(@PathVariable Long id) {
        return vagaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/vagas 
    @PostMapping
    public Vaga createVaga(@RequestBody Vaga vaga) {
        return vagaService.salvarVaga(vaga);
    }
    
    /**
     * PUT /api/vagas/{id} 
     */
    @PutMapping("/{id}")
    public ResponseEntity<Vaga> updateVaga(@PathVariable Long id, @RequestBody Vaga vagaDetails) {
        return vagaService.buscarPorId(id)
            .map(vaga -> {
                vaga.setTitulo(vagaDetails.getTitulo());
                // Você pode precisar de um DTO para atualizar a Empresa ou garantir que ela seja
                // corretamente carregada e associada aqui.
                // vaga.setEmpresa(vagaDetails.getEmpresa()); 
                
                Vaga updatedVaga = vagaService.salvarVaga(vaga);
                return ResponseEntity.ok(updatedVaga);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/vagas/{id} 
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVaga(@PathVariable Long id) {
        if (vagaService.buscarPorId(id).isPresent()) {
            vagaService.deletarVaga(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build(); 
    }
} 