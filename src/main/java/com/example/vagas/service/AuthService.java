package com.example.vagas.service;

import com.example.vagas.model.Perfil; 
import com.example.vagas.model.User;
import com.example.vagas.model.PasswordResetToken;
import com.example.vagas.repository.UserRepository;
import com.example.vagas.repository.PerfilRepository; 
import com.example.vagas.repository.PasswordResetTokenRepository;
import com.example.vagas.dto.AuthResponse; 
import com.example.vagas.security.JwtService; 

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import java.util.UUID; 
import java.time.LocalDateTime;
import org.springframework.security.authentication.BadCredentialsException; 

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordResetTokenRepository tokenRepository;
    
    private final TfaService tfaService; 

    @Transactional
    public User registerUser(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        User newUser = userRepository.save(user);

        Perfil perfil = new Perfil(newUser, newUser.getUsername());
        perfilRepository.save(perfil);
        
        newUser.setPerfil(perfil); 

        return newUser;
    }
    
    // =========================================================================
    // MODIFICADO: LOGIN INICIAL (Passo 1 - Checagem de 2FA)
    // =========================================================================
    public Optional<AuthResponse> authenticateUser(String username, String password) {
        // CORRIGIDO: O método agora existe no UserRepository
        Optional<User> userOptional = userRepository.findByUsernameOrEmail(username, username); 

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                
                // 1. CHECAGEM DE 2FA
                if (user.isTfaEnabled()) {
                    return Optional.of(AuthResponse.builder()
                            .username(user.getUsername())
                            .userId(user.getId())
                            .isTfaRequired(true)
                            .isSecondStepRequired(true)
                            .token(null)
                            .build());
                }

                // 2. 2FA desativado
                String token = jwtService.generateToken(user.getUsername()); 

                AuthResponse response = AuthResponse.builder()
                        .token(token)
                        .username(user.getUsername())
                        .userId(user.getId())
                        .isTfaRequired(false)
                        .isSecondStepRequired(false)
                        .build();

                return Optional.of(response);
            }
        }
        
        return Optional.empty();
    }
    
    // =========================================================================
    // NOVO: LOGIN 2FA (Passo 2 - Validação do OTP)
    // =========================================================================
    public AuthResponse authenticateTfa(String username, String otpCode) {
        // CORRIGIDO: O método agora existe no UserRepository
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado."));

        if (!user.isTfaEnabled()) {
             throw new IllegalStateException("2FA não está ativo para este usuário.");
        }
        
        if (!tfaService.isOtpValid(user.getTfaSecret(), otpCode)) {
            throw new BadCredentialsException("Código OTP inválido.");
        }

        String token = jwtService.generateToken(user.getUsername());
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .userId(user.getId())
                .isTfaRequired(true)
                .isSecondStepRequired(false)
                .build();
    }
    
    // =========================================================================
    // RESTAURADO: MÉTODOS DE RECUPERAÇÃO DE SENHA (Resolve os avisos de não uso)
    // =========================================================================
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email: " + email));

        String tokenValue = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);

        PasswordResetToken resetToken = new PasswordResetToken(tokenValue, user, expiryDate);
        tokenRepository.save(resetToken);

        String resetLink = "http://sua-url-frontend/reset-password?token=" + tokenValue;

        try {
            // Este método usa o emailService
            emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        } catch (Exception e) {
            System.err.println("Falha ao enviar e-mail para " + user.getEmail() + ": " + e.getMessage());
        }
    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws Exception {
        // Este método usa o tokenRepository
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Token inválido ou expirado."));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken); 
            throw new Exception("Token expirado.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }
}