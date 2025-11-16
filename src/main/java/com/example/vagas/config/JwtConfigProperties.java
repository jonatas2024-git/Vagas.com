package com.example.vagas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// Mapeia o prefixo 'app.security.jwt' do application.properties
@Component
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtConfigProperties {

    // Mapeia 'app.security.jwt.secret'
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}