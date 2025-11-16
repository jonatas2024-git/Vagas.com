package com.example.vagas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envia um e-mail de recuperação de senha para o usuário.
     * * @param toEmail O endereço de e-mail do destinatário.
     * @param resetLink O link completo de redefinição de senha (com o token).
     */
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Recuperação de Senha - Vagas.com");
        
        // O texto da mensagem usa o link completo fornecido pelo AuthService.
        message.setText("Olá,\n\n" +
                        "Você solicitou a recuperação de senha. Use o link abaixo para criar uma nova:\n\n" +
                        resetLink +
                        "\n\nO link expira em 1 hora.\n\n" +
                        "Se você não solicitou esta alteração, ignore este e-mail.");


        mailSender.send(message);
    }
}