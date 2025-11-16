package com.example.vagas.controller;

import com.example.vagas.dto.LoginRequest;
import com.example.vagas.dto.ForgotRequest; 
import com.example.vagas.dto.ResetRequest;  
import com.example.vagas.model.User;
import com.example.vagas.service.AuthService;
import com.example.vagas.dto.TfaLoginRequest; // NOVO
import com.example.vagas.dto.AuthResponse; // NOVO

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

// Importações do Swagger/OpenAPI 3
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para registro, login (JWT) e recuperação de senha.")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ... (Método register permanece o mesmo)
    @Operation(summary = "Registrar novo Usuário", 
               description = "Cria uma nova conta de usuário no sistema.")
    @RequestBody(content = @Content(schema = @Schema(implementation = User.class)), required = true, description = "Detalhes do novo usuário (nome, email, senha).")
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Email já cadastrado ou dados inválidos.")
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

    // =========================================================================
    // MODIFICADO: LOGIN INICIAL (Passo 1 - Checagem de 2FA)
    // =========================================================================
    @Operation(summary = "Login e Geração de Token JWT (Passo 1)", 
               description = "Autentica credenciais. Se o 2FA for exigido, retorna 'isSecondStepRequired: true' e *não* o Token.")
    @RequestBody(content = @Content(schema = @Schema(implementation = LoginRequest.class)), required = true, description = "Credenciais de login.")
    @ApiResponse(responseCode = "200", description = "Login bem-sucedido. Retorna o token OU o requisito de 2FA.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        // O mapeamento agora retorna o AuthResponse que contém o status do 2FA
        return authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword())
                .<ResponseEntity<AuthResponse>>map(ResponseEntity::ok) 
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // =========================================================================
    // NOVO: LOGIN 2FA (Passo 2 - Validação do OTP)
    // =========================================================================
    @Operation(summary = "Login 2FA - Validação do OTP (Passo 2)", 
               description = "Para usuários com 2FA ativo, este endpoint valida o código OTP e, em caso de sucesso, retorna o Token JWT.")
    @RequestBody(content = @Content(schema = @Schema(implementation = TfaLoginRequest.class)), required = true, description = "Username/Email e Código OTP.")
    @ApiResponse(responseCode = "200", description = "Código OTP válido. Retorna o Token JWT.")
    @ApiResponse(responseCode = "401", description = "Código OTP inválido.")
    @PostMapping("/tfa-login")
    public ResponseEntity<AuthResponse> tfaLogin(@Valid @RequestBody TfaLoginRequest tfaRequest) {
        try {
            // O service busca o usuário e valida o código OTP
            AuthResponse response = authService.authenticateTfa(tfaRequest.getUsername(), tfaRequest.getCode());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // BadCredentialsException, IllegalStateException ou outra exceção
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }
    }
    
    // ... (Métodos forgot-password e reset-password permanecem os mesmos)
    @Operation(summary = "Solicitar Recuperação de Senha", 
               description = "Envia um link/token de redefinição de senha para o email fornecido.")
    @RequestBody(content = @Content(schema = @Schema(implementation = ForgotRequest.class)), required = true, description = "Email do usuário.")
    @ApiResponse(responseCode = "200", description = "Notificação de envio de email.")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotRequest forgotRequest) {
        try {
            authService.requestPasswordReset(forgotRequest.getEmail());
            return ResponseEntity.ok().body("Se o email estiver cadastrado, um link de recuperação foi enviado.");
        } catch (Exception e) {
            return ResponseEntity.ok().body("Se o email estiver cadastrado, um link de recuperação foi enviado.");
        }
    }
    
    @Operation(summary = "Redefinir Senha", 
               description = "Executa a redefinição de senha com o token.")
    @RequestBody(content = @Content(schema = @Schema(implementation = ResetRequest.class)), required = true, description = "Token de redefinição e nova senha.")
    @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso.")
    @ApiResponse(responseCode = "400", description = "Token inválido ou expirado.")
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