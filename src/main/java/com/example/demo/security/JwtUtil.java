package com.example.demo.security;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Component
public class JwtUtil {

    
    private final String SECRET = "secretKeyForJwtGenerationAndValidation1234567890"; 
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generarToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().stream().findFirst().get().getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) 
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraerUsuario(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token inválido", e);
        }
      
    }

    public boolean validarToken(String token, UserDetails userDetails) {
        try {
            final String username = extraerUsuario(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpirado(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpirado(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

