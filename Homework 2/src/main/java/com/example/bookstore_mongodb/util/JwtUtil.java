package com.example.bookstore_mongodb.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
        @Value("${jwt.secret}")
        private String secret;

        @Value("${jwt.expiration}")
        private long expiration;

        private SecretKey getSigningKey() {
            return Keys.hmacShaKeyFor(secret.getBytes());
        }

        // generate a token
    public String generateToken(UserDetails userDetails) {
            return Jwts.builder()
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSigningKey())
                    .compact();
    }

    public String extractUsername(String token) {
            return extractClaims(token).getSubject();
    }

    public Claims extractClaims(String token) {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
            final String username = extractUsername(token);

            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
            return extractClaims(token).getExpiration().before(new Date());
    }


}
