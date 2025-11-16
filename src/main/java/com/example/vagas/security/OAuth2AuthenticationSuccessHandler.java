package com.example.vagas.security; 

import com.example.vagas.AppProperties; 
//import com.example.vagas.security.JwtService; --> Linha removida!
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User; 
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final AppProperties appProperties; 

    public OAuth2AuthenticationSuccessHandler(JwtService jwtService, AppProperties appProperties) {
        this.jwtService = jwtService;
        this.appProperties = appProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        // **CORREÇÃO CRÍTICA**: Obtém o email do usuário autenticado no OAuth2
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String userEmail = oAuth2User.getAttribute("email");
        
        // 1. Gera o JWT usando o email
        String token = jwtService.generateToken(userEmail);
        
        // 2. Pega a URI de redirecionamento do seu AppProperties (Frontend URI)
        String redirectUri = appProperties.getAuthorizedRedirectUri();

        // 3. Constrói a URL de redirecionamento para o Frontend com o token
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token) 
                .build().toUriString();

        // 4. Redireciona o usuário (Frontend receberá o JWT)
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}