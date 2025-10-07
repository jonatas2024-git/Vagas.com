package com.example.vagas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();

        String frontendUrl = "http://localhost:3000/reset-password"; 
        String resetLink = frontendUrl + "?token=" + token;

        message.setTo(toEmail);
        message.setSubject("Recuperação de Senha - Vagas.com");
        message.setText("Olá,\n\n" +
                        "Você solicitou a recuperação de senha. Use o link abaixo para criar uma nova:\n\n" +
                        resetLink +
                        "\n\nO link expira em 1 hora.\n\n" +
                        "Se você não solicitou esta alteração, ignore este e-mail.");


        mailSender.send(message);
    }
}