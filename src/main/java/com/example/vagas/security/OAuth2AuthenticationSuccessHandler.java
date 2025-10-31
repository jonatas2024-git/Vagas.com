package com.example.vagas.security; 

import com.example.vagas.AppProperties; // Importe a classe AppProperties
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// REMOVIDO: import org.springframework.beans.factory.annotation.Value; // Não precisamos mais do @Value
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final AppProperties appProperties; // NOVO: Injete a classe AppProperties

    // Construtor para injeção de dependência
    public OAuth2AuthenticationSuccessHandler(JwtService jwtService, AppProperties appProperties) {
        this.jwtService = jwtService;
        this.appProperties = appProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        String username = authentication.getName(); 
        
        String token = jwtService.generateToken(username);
        
        // CORREÇÃO: Pega a URI de redirecionamento do seu AppProperties
        String redirectUri = appProperties.getAuth().getAuthorizedRedirectUri();

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token) 
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}