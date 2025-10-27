package com.example.vagas.security;

import com.example.vagas.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod; --> Linha comentada, não habilitar. 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    // Injeta o serviço de usuário para o OAuth2
    @Autowired 
    private CustomOAuth2UserService customOAuth2UserService; 
    
    // Opcional: injetar o serviço de logout se você tiver um.
    
    // Configuração principal da cadeia de filtros de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            // 1. Desativa CSRF (Comum para APIs REST)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Define a política de gerenciamento de sessão como STATELESS (para JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 3. Configuração de Autorização de Requisições
            .authorizeHttpRequests(authorize -> authorize
                // Acesso PÚBLICO para iniciar o fluxo OAuth2 e a rota de API de Vagas (para teste)
                .requestMatchers("/oauth2/**", "/login/**", "/error").permitAll()
                
                // Rotas de API que exigem autenticação (usando o filtro JWT)
                // Se você quiser testar suas APIs sem login, MANTENHA A LINHA ABAIXO COMENTADA ou altere para .permitAll()
                // EX: .requestMatchers("/api/vagas/**", "/api/empresas/**").authenticated()
                
                // TEMPORARIAMENTE: Liberamos todas as APIs para teste, ignorando o JWT/OAuth2. 
                // APÓS O TESTE, COMENTE/REMOCENTE ESTA LINHA:
                .requestMatchers("/api/**").permitAll() 

                // Todas as outras requisições exigem autenticação
                .anyRequest().authenticated()
            )
            
            // 4. Configuração do Fluxo OAuth2 (Login com Google)
            .oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2AuthenticationSuccessHandler) 
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService) 
                )
                // Opcional: .failureUrl("/login?error=true")
            )
            
            // 5. Adiciona o filtro JWT antes do filtro padrão do Spring Security
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}