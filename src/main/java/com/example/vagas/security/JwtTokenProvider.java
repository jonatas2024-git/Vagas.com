package com.example.vagas.security;

import com.example.vagas.config.JwtConfigProperties;
import com.example.vagas.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long EXPIRATION_TIME = 3600000; 

    public JwtTokenProvider(JwtConfigProperties jwtConfigProperties) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(
                jwtConfigProperties.getSecret()));
    }

   
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(user.getUsername()) 
                .issuedAt(now)
                .expiration(expiryDate) 
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
}