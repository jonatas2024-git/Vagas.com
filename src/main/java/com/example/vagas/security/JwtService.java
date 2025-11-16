package com.example.vagas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey; 
// REMOVIDO: import java.security.Key; // Não é mais usado
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.security.jwt.secret}")
    private String SECRET_KEY;
    
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24; 


    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            String username
    ) {
        return Jwts
                .builder()
                .claims(extraClaims) 
                .subject(username) 
                .issuedAt(new Date(System.currentTimeMillis())) 
                // Define o tempo de expiração
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME)) 
                .signWith(getSignInKey()) 
                .compact();
    }

    // --- Métodos de Validação e Extração ---

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

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
        return Jwts
                .parser()
                .verifyWith(getSignInKey()) 
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // --- Métodos Auxiliares de Chave ---

    private SecretKey getSignInKey() { 
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes); 
    }
}