package com.log430.brockerx.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;


@Service
public class JwtService {

    private static final String SECRET_KEY = String.valueOf(Keys.secretKeyFor(SignatureAlgorithm.HS256));
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1h

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(
                new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(getSigningKey(),
                                                                                 SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
