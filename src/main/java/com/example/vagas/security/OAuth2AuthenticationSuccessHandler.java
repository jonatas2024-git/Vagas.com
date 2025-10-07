package com.example.vagas.security.oauth2;

import com.example.vagas.security.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    
    @Value("${app.oauth2.redirectUri}")
    private String redirectUri; 

    @Autowired
    public OAuth2AuthenticationSuccessHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        
        String username = authentication.getName(); 
        
        String token = jwtService.generateToken(username);

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token) 
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}