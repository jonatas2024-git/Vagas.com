package com.example.vagas.controller;

import com.example.vagas.model.Empresa;
import com.example.vagas.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<Empresa> createEmpresa(@RequestBody Empresa empresa) {
        Empresa newEmpresa = empresaService.createEmpresa(empresa);
        return new ResponseEntity<>(newEmpresa, HttpStatus.CREATED);
    }

    // Todas as empresas - Sugestão
    @GetMapping
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        List<Empresa> todasEmpresas = empresaService.listarEmpresas();
        return new ResponseEntity<>(todasEmpresas, HttpStatus.OK);
    }

    // Empresa pelo id - Sugestão
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable Long id) {
        return empresaService.buscarEmpresaPorId(id)
                .map(empresa -> new ResponseEntity<>(empresa, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> updateEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {
        try {
            Empresa updateEmpresa = empresaService.atualizarEmpresa(id, empresa);
            return new ResponseEntity<>(updateEmpresa, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        try {
            empresaService.deletarEmpresaPorId(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}