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
            // 🔒 Desabilita CSRF para APIs REST
            .csrf(csrf -> csrf.disable())

            // ⚠️ IMPORTANTE: SessionCreationPolicy deve ser IF_REQUIRED para permitir OAuth2 redirection
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            // 🔐 Configuração das permissões
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/vagas/**").permitAll()
                .requestMatchers("/api/empresas/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/oauth2/authorization/**", "/login/oauth2/code/*").permitAll()
                .requestMatchers("/oauth2/redirect").permitAll()
                .anyRequest().authenticated()
            )

            // 🧠 Segurança de cabeçalhos
            .headers(headers -> headers
                .xssProtection(xss -> {})
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                .frameOptions(frameOptions -> frameOptions.deny())
                .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000))
            )

            // ⚙️ Configuração OAuth2
            .oauth2Login(oauth2 -> oauth2
                .redirectionEndpoint(redirection -> redirection.baseUri("/login/oauth2/code/*"))
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                .successHandler(oauth2AuthenticationSuccessHandler) // ✅ Handler que redireciona ao Front-end
            );

        // 🔄 JWT Filter continua ativo
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
