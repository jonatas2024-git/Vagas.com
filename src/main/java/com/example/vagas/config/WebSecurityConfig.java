package com.example.vagas.config;

import com.example.vagas.security.JwtRequestFilter; 
import com.example.vagas.service.CustomOAuth2UserService;
import com.example.vagas.security.OAuth2AuthenticationSuccessHandler; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
           .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
           )
           .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() 
                .requestMatchers("/api/vagas", "/api/vagas/**").permitAll()
                .requestMatchers("/api/empresas", "/api/empresas/**").permitAll() 
                // **ADICIONAL**: Permitindo acesso para endpoints do Swagger/OpenAPI
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // **ADICIONAL**: Permitindo endpoints de login social
                .requestMatchers("/oauth2/authorization/**", "/login/oauth2/code/*").permitAll() 
                .anyRequest().authenticated()
            )
            .headers(headers -> headers
                .xssProtection(xss -> {}) 
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'")) 
                .frameOptions(frameOptions -> frameOptions.deny())
                .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000)) 
            )
            .oauth2Login(oauth2 -> oauth2
                // **CORREÇÃO**: Garante que o Spring Security aceite a URI de retorno do Google
                .redirectionEndpoint(redirection -> redirection
                    .baseUri("/login/oauth2/code/*")
                )
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
                // Usando o Handler customizado para redirecionar ao Frontend com o token
                .successHandler(oauth2AuthenticationSuccessHandler) 
            );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}