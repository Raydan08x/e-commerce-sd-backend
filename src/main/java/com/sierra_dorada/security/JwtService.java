package com.sierra_dorada.security;

import com.sierra_dorada.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;
    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }
    public String generar(Usuario usuario) {
        Date ahora = new Date();
        return Jwts.builder().subject(usuario.getEmail())
            .claim("id", usuario.getId()).claim("rol", usuario.getRol().name())
            .issuedAt(ahora).expiration(new Date(ahora.getTime() + expirationMs))
            .signWith(key).compact();
    }
    public String obtenerEmail(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }
    public boolean esValido(String token) {
        try { obtenerEmail(token); return true; } catch (JwtException | IllegalArgumentException ex) { return false; }
    }
}
