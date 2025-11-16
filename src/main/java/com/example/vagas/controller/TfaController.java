package com.example.vagas.controller;

import com.example.vagas.service.TfaService;
import com.example.vagas.dto.TfaQrCodeResponse;
import com.example.vagas.dto.TfaCodeRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

// Importações do Swagger/OpenAPI 3
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/tfa")
@RequiredArgsConstructor
@Tag(name = "2FA (Autenticação de Dois Fatores)", description = "Endpoints para gerenciamento do 2FA do usuário logado.")
@SecurityRequirement(name = "bearerAuth") // Requer JWT para todas as operações
public class TfaController {

    private final TfaService tfaService;

    /**
     * Passo 1 de Ativação: Inicia a ativação do 2FA, gera um segredo e retorna o URL do QR Code.
     * O usuário deve ler este QR Code no Google Authenticator (ou similar).
     */
    @Operation(summary = "Gerar QR Code 2FA",
               description = "Inicia o processo de ativação 2FA, gerando um novo 'secret' e retornando o URL 'otpauth://' para criação da imagem do QR Code.")
    @ApiResponse(responseCode = "200", description = "URL do QR Code gerado com sucesso.")
    @ApiResponse(responseCode = "401", description = "Não autenticado.")
    @PostMapping("/generate")
    public ResponseEntity<TfaQrCodeResponse> generateQrCode() {
        String qrCodeUrl = tfaService.generateTfaSecretAndQrCodeUrl();
        
        TfaQrCodeResponse response = new TfaQrCodeResponse();
        response.setQrCodeUrl(qrCodeUrl);

        return ResponseEntity.ok(response);
    }

    /**
     * Passo 2 de Ativação: O usuário envia o código OTP gerado pelo aplicativo para ativar o 2FA.
     */
    @Operation(summary = "Ativar 2FA",
               description = "Conclui a ativação do 2FA. O código (OTP) deve ser o gerado pelo aplicativo Authenticator, após a leitura do QR Code.")
    @ApiResponse(responseCode = "200", description = "2FA ativado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Código OTP inválido ou segredo 2FA não foi gerado.")
    @ApiResponse(responseCode = "401", description = "Não autenticado.")
    @PostMapping("/enable")
    public ResponseEntity<Void> enableTfa(@Valid @RequestBody TfaCodeRequest request) {
        tfaService.enableTfa(request.getCode());
        return ResponseEntity.ok().build();
    }

    /**
     * Desativa o 2FA. Requer o código OTP atual para confirmar a identidade.
     */
    @Operation(summary = "Desativar 2FA",
               description = "Desativa o 2FA. É necessário fornecer o código OTP atual para confirmação de segurança.")
    @ApiResponse(responseCode = "200", description = "2FA desativado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Código OTP inválido ou 2FA já estava desativado.")
    @ApiResponse(responseCode = "401", description = "Não autenticado.")
    @PostMapping("/disable")
    public ResponseEntity<Void> disableTfa(@Valid @RequestBody TfaCodeRequest request) {
        tfaService.disableTfa(request.getCode());
        return ResponseEntity.ok().build();
    }
}