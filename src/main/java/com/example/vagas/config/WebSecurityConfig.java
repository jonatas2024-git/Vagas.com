package com.example.vagas.config;

import com.example.vagas.security.JwtRequestFilter; 
import com.example.vagas.service.CustomOAuth2UserService;
import com.example.vagas.security.OAuth2AuthenticationSuccessHandler; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Importado para uso com GET
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter; 

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    
    @Autowired 
    private OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF para APIs REST com JWT
            .csrf(csrf -> csrf.disable())

            // Define a política de sessão. IF_REQUIRED é necessário para o fluxo OAuth2.
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            // Configuração das permissões para amarrar a proteção JWT
            .authorizeHttpRequests(auth -> auth
                
                // === Rotas Públicas (Login, Cadastro, Documentação, OAuth2) ===
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/oauth2/authorization/**", "/login/oauth2/code/*").permitAll()
                .requestMatchers("/oauth2/redirect").permitAll()
                
                // === Rotas de Leitura Pública (GET) ===
                // Permite a busca e listagem de Vagas, Empresas (Leitura)
                .requestMatchers(HttpMethod.GET, "/api/vagas/**").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/empresas/**").permitAll()
                
                // === Rotas de Escrita/Restritas (Exigem JWT) ===
                // Todas as outras operações (POST, PUT, DELETE, PATCH, etc.) 
                // e quaisquer outros endpoints não listados acima, exigem autenticação.
                // Isso inclui a criação, atualização e deleção de Vagas e Empresas.
                .anyRequest().authenticated()
            )

            // Segurança de cabeçalhos (Mitigação de XSS/CSRF e HSTS)
            .headers(headers -> headers
                .xssProtection(xss -> {})
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                .frameOptions(frameOptions -> frameOptions.deny()) // Previne Clickjacking
                .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000)) // HSTS
            )

            // Configuração OAuth2 (login social)
            .oauth2Login(oauth2 -> oauth2
                .redirectionEndpoint(redirection -> redirection.baseUri("/login/oauth2/code/*"))
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                .successHandler(oauth2AuthenticationSuccessHandler) // Handler para gerar o JWT
            );

        // Adiciona o filtro JWT customizado ANTES do filtro de autenticação padrão
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}