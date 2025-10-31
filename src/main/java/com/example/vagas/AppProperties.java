package com.example.vagas; 

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// IMPORTAÇÕES REMOVIDAS:
//import java.util.ArrayList;
//import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.oauth2")
public class AppProperties {

    private final Auth auth = new Auth();

    public static class Auth {
        private String authorizedRedirectUri; 
        
        // Getters e Setters
        public String getAuthorizedRedirectUri() {
            return authorizedRedirectUri;
        }

        public void setAuthorizedRedirectUri(String authorizedRedirectUri) {
            this.authorizedRedirectUri = authorizedRedirectUri;
        }
    }

    public Auth getAuth() {
        return auth;
    }
}