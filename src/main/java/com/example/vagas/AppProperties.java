package com.example.vagas;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// Imports de ArrayList e List removidos, conforme acordado.

@Configuration
@ConfigurationProperties(prefix = "app.oauth2")
public class AppProperties {

    // A propriedade é mapeada diretamente para app.oauth2.authorizedRedirectUri
    private String authorizedRedirectUri; 

    // Getters e Setters
    public String getAuthorizedRedirectUri() {
        return authorizedRedirectUri;
    }

    public void setAuthorizedRedirectUri(String authorizedRedirectUri) {
        this.authorizedRedirectUri = authorizedRedirectUri;
    }
}