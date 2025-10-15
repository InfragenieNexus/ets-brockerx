package com.log430.brockerx.service;

import com.log430.brockerx.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;


@Service
public class JwtService {

    private static final String SECRET_KEY = "MaCleSecreteTr3sLonguejaelfiajsefliajsfelaisjfeiajsfeliasj12334"; // ou
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1h

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("status", user.getStatus());

        return Jwts.builder().setClaims(claims).setSubject(user.getEmail()).setIssuedAt(
                new Date(System.currentTimeMillis())).setExpiration(
                new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(getSigningKey(),
                                                                                 SignatureAlgorithm.HS256).compact();
    }


    public Long extractUserId(String token) {

        return extractAllClaims(token).get("id", Long.class);

    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }


    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }
}
