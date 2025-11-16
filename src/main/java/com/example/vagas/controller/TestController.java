package com.example.vagas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String statusCheck() {
        return "API Vagas.com: Conectada ao Supabase e Rodando em Modo Protegido!";
    }
}