package com.example.vagas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protegido")
public class ProtectedController {

    @GetMapping
    public String getProtectedData() {
        // Esta rota só deve ser alcançada se o JWT for válido
        return "Dados confidenciais liberados pelo JWT!";
    }
}