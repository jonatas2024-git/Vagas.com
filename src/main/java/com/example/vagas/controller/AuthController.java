package com.example.vagas.controller;

import com.example.vagas.dto.LoginRequest;
// REMOVIDO: import com.example.vagas.dto.AuthResponse; 
import com.example.vagas.dto.ForgotRequest; 
import com.example.vagas.dto.ResetRequest;  
import com.example.vagas.model.User;
import com.example.vagas.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User newUser = authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // O tipo AuthResponse é inferido de authService.authenticateUser() e não requer o import explícito.
        return authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword())
                .<ResponseEntity<?>>map(authResponse -> 
                        ResponseEntity.ok(authResponse)) 
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Credenciais inválidas")); 
    }
    
    /**
     * Endpoint para solicitar o link de recuperação de senha por email.
     * Rota: POST /api/auth/forgot-password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotRequest forgotRequest) {
        try {
            authService.requestPasswordReset(forgotRequest.getEmail());
            
            return ResponseEntity.ok().body("Se o email estiver cadastrado, um link de recuperação foi enviado.");
        } catch (Exception e) {
            return ResponseEntity.ok().body("Se o email estiver cadastrado, um link de recuperação foi enviado.");
        }
    }
    
    /**
     * Endpoint para executar a redefinição de senha com o token.
     * Rota: POST /api/auth/reset-password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetRequest resetRequest) {
        try {
            authService.resetPassword(resetRequest.getToken(), resetRequest.getNewPassword());
            return ResponseEntity.ok().body("Senha alterada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}