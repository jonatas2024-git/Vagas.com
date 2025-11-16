package com.example.vagas.config;

// import org.springframework.context.annotation.Bean;  <-- Pode ser removido
// import org.springframework.context.annotation.Configuration; <-- REMOVIDO!
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// @Configuration <-- REMOVIDO PARA DESATIVAR A CLASSE DE CONFIGURAÇÃO!
public class SecurityConfig {

    // @Bean <-- OPCIONALMENTE COMENTADO
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desativa CSRF (necessário para APIs REST)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // Libera todos os endpoints de autenticação
                .anyRequest().authenticated() // Exige login para os demais
            );
        return http.build();
    }
}