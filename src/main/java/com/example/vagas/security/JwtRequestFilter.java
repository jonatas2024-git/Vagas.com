package com.example.vagas.security;

//import com.example.vagas.security.JwtService; 
import io.jsonwebtoken.ExpiredJwtException; 
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtService jwtService; 
    
    @Autowired
    private UserDetailsService userDetailsService; 

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, // CORRIGIDO: Adiciona @NonNull
            @NonNull HttpServletResponse response, // CORRIGIDO: Adiciona @NonNull
            @NonNull FilterChain filterChain) // CORRIGIDO: Adiciona @NonNull
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String userEmail = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            
            try {
                userEmail = jwtService.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token expirado. URI: {}", request.getRequestURI()); 
            } catch (SignatureException e) {
                logger.error("JWT Token com assinatura inválida. URI: {}", request.getRequestURI(), e); 
            } catch (Exception e) {
                 logger.error("Erro ao processar JWT. URI: {}", request.getRequestURI(), e); 
            }
        }
        
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                    
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                    );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                logger.error("Falha ao carregar UserDetails ou validar token: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}