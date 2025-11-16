package com.example.vagas.controller;

import com.example.vagas.dto.VagaFilterDTO;
import com.example.vagas.model.Vaga;
import com.example.vagas.service.VagaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/vagas")
@RequiredArgsConstructor
public class VagaController {

    private final VagaService vagaService;

    @PostMapping
    public ResponseEntity<Vaga> criar(@RequestBody Vaga vaga) {
        Vaga criado = vagaService.criar(vaga);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vaga> atualizar(@PathVariable UUID id, @RequestBody Vaga vaga) {
        Vaga atualizado = vagaService.atualizar(id, vaga);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        vagaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vaga> getById(@PathVariable UUID id) {
        Vaga vaga = vagaService.buscarPorId(id);
        return ResponseEntity.ok(vaga);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<Vaga>> buscar(VagaFilterDTO filters, Pageable pageable) {
        Page<Vaga> page = vagaService.buscarComFiltros(filters, pageable);
        return ResponseEntity.ok(page);
    }

    // lista simples paginada
    @GetMapping
    public ResponseEntity<Page<Vaga>> listar(Pageable pageable) {
        Page<Vaga> page = vagaService.buscarComFiltros(new VagaFilterDTO(), pageable);
        return ResponseEntity.ok(page);
    }
}
