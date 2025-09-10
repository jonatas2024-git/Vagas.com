import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class ProvedorJwt {

    // Segredo forte, idealmente longo e aleatório
    @Value("${jwt.segredo}")
    private String segredo;

    @Value("${jwt.expiracao}")
    private long expiracao; // em milissegundos

    @Value("${jwt.expiracaoRefresh}")
    private long expiracaoRefresh; // em milissegundos

    private Key getChave() {
        return Keys.hmacShaKeyFor(segredo.getBytes());
    }

    // Gera token JWT normal
    public String gerarToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiracao))
                .signWith(getChave(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Gera refresh token
    public String gerarRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiracaoRefresh))
                .signWith(getChave(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Valida token JWT
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getChave()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Pega o email (subject) do token
    public String pegarEmailDoToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getChave())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
