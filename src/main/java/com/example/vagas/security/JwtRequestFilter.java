package com.example.vagas.security;

import com.example.vagas.security.jwt.JwtService; 
import io.jsonwebtoken.ExpiredJwtException; 
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JwtService jwtService; 
    
    @Autowired
    private UserDetailsService userDetailsService; 

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String userEmail = null;

        // 1. Extração do Token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            
            try {
                // Tenta extrair o email (subject)
                userEmail = jwtService.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                // CORREÇÃO: Passa o objeto de exceção 'e' como argumento para o logger.warn
                logger.warn("JWT Token expirado: {}", e); // Linha 48 Corrigida
            } catch (SignatureException e) {
                // CORREÇÃO: Passa o objeto de exceção 'e' como argumento para o logger.error
                logger.error("JWT Token com assinatura inválida: {}", e); // Linha 51 Corrigida
            } catch (Exception e) {
                // CORREÇÃO: Passa o objeto de exceção 'e' como argumento para o logger.error
                 logger.error("Erro ao extrair username do JWT: {}", e); // Linha 54 Corrigida
            }
        }

        // 2. Validação e Configuração do Contexto de Segurança
        // Verifica se o email foi extraído e se o usuário AINDA não está autenticado
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Carrega os detalhes do usuário a partir do UserDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Valida o token (checa validade e expiração novamente)
            if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                
                // Cria o objeto de autenticação
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()
                );
                
                // Adiciona detalhes da requisição
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Define o usuário como autenticado
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 3. Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}