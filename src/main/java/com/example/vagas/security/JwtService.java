package com.example.vagas.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Injeta a chave secreta do application.properties
    @Value("${app.security.jwt.secret}")
    private String SECRET_KEY;
    
    // Tempo de expiração do token (Exemplo: 24 horas em milissegundos)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; 

    // --- Métodos de Geração de Token ---

    public String generateToken(String username) {
        // Usa um mapa vazio para claims adicionais (se necessário)
        return generateToken(new HashMap<>(), username);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            String username
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Define o tempo de expiração
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) 
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Assina com a chave secreta
                .compact();
    }

    // --- Métodos de Validação e Extração ---

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Nota: O username aqui deve ser o UserDetails.getUsername()
    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // CORREÇÃO: Usando a sintaxe moderna do JJWT para o parser
        return Jwts
                .parser()
                .setSigningKey(getSignInKey()) // Define a chave de assinatura
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // --- Métodos Auxiliares de Chave ---

    private Key getSignInKey() {
        // Decodifica a chave de base64 (string) para bytes
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        // Cria a chave de segurança HMAC
        return Keys.hmacShaKeyFor(keyBytes);
    }
}