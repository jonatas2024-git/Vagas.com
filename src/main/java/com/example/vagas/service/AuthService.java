package com.example.vagas.service;

import com.example.vagas.model.Perfil; 
import com.example.vagas.model.User;
import com.example.vagas.model.PasswordResetToken;
import com.example.vagas.repository.UserRepository;
import com.example.vagas.repository.PerfilRepository; 
import com.example.vagas.repository.PasswordResetTokenRepository;
import com.example.vagas.dto.AuthResponse; 
import com.example.vagas.security.jwt.JwtService; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 
import java.util.Optional;
import java.util.UUID; 
import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired 
    private JwtService jwtService;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

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
        
        // Ajuste: Garante que o objeto Perfil está ligado ao User (se for um campo)
        newUser.setPerfil(perfil); 

        return newUser;
    }
    
    public Optional<AuthResponse> authenticateUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                
                String token = jwtService.generateToken(user.getUsername()); 

                AuthResponse response = new AuthResponse();
                // setToken() e setUsername() agora existem devido ao @Data em AuthResponse
                response.setToken(token);
                response.setUsername(user.getUsername());

                return Optional.of(response);
            }
        }
        
        return Optional.empty(); 
    }
    
    @Transactional
    public void requestPasswordReset(String username) throws Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Usuário não encontrado."));

        // 1. Gerar e Salvar o Token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1); 

        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryDate);
        tokenRepository.save(resetToken);

        // 2. Enviar E-mail
        emailService.sendResetPasswordEmail(user.getUsername(), token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws Exception {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Token inválido ou expirado."));

        // getExpiryDate() agora existe devido ao @Data em PasswordResetToken
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            // Removendo o token expirado para limpeza
            tokenRepository.delete(resetToken); 
            throw new Exception("Token expirado.");
        }

        // 1. Atualizar a Senha
        // getUser() agora existe devido ao @Data em PasswordResetToken
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 2. Invalidar/Deletar o Token
        tokenRepository.delete(resetToken);
    }
}