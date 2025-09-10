import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {

    private final ServicoOAuth2Google servicoOAuth2Google;
    private final FiltroJwt filtroJwt;

    public SecurityConfig(ServicoOAuth2Google servicoOAuth2Google, FiltroJwt filtroJwt) {
        this.servicoOAuth2Google = servicoOAuth2Google;
        this.filtroJwt = filtroJwt;
    }

    @Bean
    public PasswordEncoder codificadorSenha() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configuracao(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
                .requestMatchers("/login/**", "/oauth2/**", "/auth/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .oauth2Login()
                .userInfoEndpoint()
                    .userService(servicoOAuth2Google);

        // Adiciona filtro JWT antes do UsernamePasswordAuthenticationFilter
        http.addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        // ?? Cabe�alhos de seguran�a
        http.headers()
            .contentSecurityPolicy("default-src 'self';") // CSP
            .and()
            .httpStrictTransportSecurity()                 // HSTS
                .includeSubDomains(true)
                .maxAgeInSeconds(31536000)
            .and()
            .frameOptions().deny();                        // X-Frame-Options

        return http.build();
    }
@Configuration
@EnableWebSecurity
public class SegurtiyConfig {

    private final FiltroJwt filtroJwt;

    public SegurtiyConfig(FiltroJwt filtroJwt) {
        this.filtroJwt = filtroJwt;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Desativa CSRF para JWT
            .csrf().disable()

            // Sess�o sem estado, usamos JWT
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            // Regras de acesso
            .authorizeHttpRequests()
            .requestMatchers("/auth/**").permitAll() // endpoints de autentica��o p�blicos
            .requestMatchers(HttpMethod.GET, "/vagas/**").permitAll() // listagem de vagas aberta
            .anyRequest().authenticated()
            .and()

            // Cabe�alhos de seguran�a
            .headers()
            .contentSecurityPolicy("default-src 'self'; script-src 'self'; style-src 'self';")
            .and()
            .frameOptions().deny()
            .and()
            .httpStrictTransportSecurity()
            .includeSubDomains(true)
            .maxAgeInSeconds(31536000);

        // Adiciona filtro JWT antes do UsernamePasswordAuthenticationFilter
        http.addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
}
