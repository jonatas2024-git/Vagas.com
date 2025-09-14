package com.example.vagas.controller;

import com.example.vagas.model.Empresa;
import com.example.vagas.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}