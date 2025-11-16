package com.example.vagas.service;

import com.example.vagas.model.User;
import com.example.vagas.repository.UserRepository;
import com.example.vagas.security.SecurityUtils;
import com.example.vagas.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

// Importações TOTP/QR Code (Você precisará de uma biblioteca, como base32, para gerar o Secret e o Código)
// *** NOTA: Em um ambiente real, substitua esta lógica pela biblioteca TOTP de sua preferência. ***
// *** Para este exemplo, usaremos Strings placeholder e uma função de validação simulada. ***

@Service
@RequiredArgsConstructor
public class TfaService {

    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    // Nome da aplicação que aparecerá no Google Authenticator
    @Value("${application.name:Vagas.com}")
    private String issuer; 

    /**
     * 1. Geração do Segredo e QR Code (Passo de Ativação 1)
     * Deve ser chamado pelo usuário logado para iniciar a ativação do 2FA.
     * Retorna o URL do QR Code (formato otpauth://...).
     */
    @Transactional
    public String generateTfaSecretAndQrCodeUrl() {
        UUID userId = securityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário logado não encontrado."));

        // 1. Gerar Secret (Ex: usando Base32)
        String secret = generateNewTfaSecret(); // Simulação
        
        // 2. Salvar o segredo temporariamente no User (ainda não ativado)
        user.setTfaSecret(secret);
        userRepository.save(user);

        // 3. Gerar URL do QR Code (usando o protocolo otpauth)
        // Este URL é o que o Front-end transformará em imagem QR Code.
        String qrCodeUrl = buildQrCodeUrl(user.getEmail(), secret);

        return qrCodeUrl;
    }

    /**
     * 2. Ativar 2FA (Passo de Ativação 2)
     * O usuário insere o código do Authenticator App para confirmar.
     */
    @Transactional
    public void enableTfa(String code) {
        UUID userId = securityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário logado não encontrado."));
        
        String secret = user.getTfaSecret();
        if (secret == null) {
             throw new IllegalStateException("O segredo 2FA deve ser gerado antes de ser ativado.");
        }
        
        // 1. Validar o código OTP
        if (!isOtpValid(secret, code)) {
            throw new IllegalArgumentException("Código de verificação (OTP) inválido.");
        }

        // 2. Se o código for válido, ativa permanentemente
        user.setTfaEnabled(true);
        userRepository.save(user);
    }
    
    /**
     * 3. Desativar 2FA (Com o código OTP atual)
     */
    @Transactional
    public void disableTfa(String code) {
        UUID userId = securityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário logado não encontrado."));

        if (!user.isTfaEnabled()) {
            throw new IllegalStateException("O 2FA já está desativado.");
        }

        // 1. Validar o código OTP para evitar desativação por terceiros
        if (!isOtpValid(user.getTfaSecret(), code)) {
            throw new IllegalArgumentException("Código de verificação (OTP) inválido.");
        }

        // 2. Desativa e limpa o segredo
        user.setTfaEnabled(false);
        user.setTfaSecret(null);
        userRepository.save(user);
    }
    
    /**
     * Função auxiliar para construir o URL no formato otpauth://
     */
    private String buildQrCodeUrl(String email, String secret) {
        // Formato: otpauth://totp/Issuer:userEmail?secret=SECRET&issuer=Issuer
        String appName = issuer.replace(" ", "%20");
        String userLabel = email;
        
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s",
                             appName, userLabel, secret, appName);
    }

    // =========================================================================
    // LÓGICA MOCK (A ser substituída pela Biblioteca TOTP)
    // =========================================================================

    /**
     * Simulação da Geração do Secret (deve ser um valor Base32)
     */
    private String generateNewTfaSecret() {
        // Em produção, use uma função segura de Base32
        return "MOCKBASE32SECRET" + System.currentTimeMillis() % 1000;
    }

    /**
     * Simulação da Validação do OTP (principal função TOTP)
     */
    public boolean isOtpValid(String secret, String code) {
        // Em produção, chame o método validate da biblioteca TOTP.
        // O valor do código de teste será "123456" para esta simulação.
        // O TOTP tem uma tolerância de ±30 segundos (time step).
        System.out.println("--- DICA ---: Em produção, este método validaria o código OTP.");
        return "123456".equals(code); // Retorna true se o código for 123456 (apenas para testes)
    }
}