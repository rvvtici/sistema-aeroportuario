package com.airport.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "glider-secret-key-must-be-at-least-32-chars!!";
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 8; // 8 horas

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String gerar(String login, String role, String aeroportoIata) {
        return Jwts.builder()
                .setSubject(login)
                .claim("role", role)
                .claim("aeroportoIata", aeroportoIata)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extrair(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validar(String token) {
        try {
            extrair(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}