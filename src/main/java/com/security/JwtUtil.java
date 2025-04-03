package com.security;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtUtil {
	
	// Secret key for signing the JWT, you should store this securely (e.g., in application.properties)
    @Value("${jwt.secret}")
    private String secretKey;

    // JWT expiration time (1 hour for example)
    @Value("${jwt.expiration}")
    private long validityInMilliseconds = 3600000; // 1 hour

    // Generate a JWT token
    public String generateToken(String username,List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Extract the username from the JWT
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // Validate the JWT token
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Parse JWT and extract claims
    private Claims parseClaims(String token) {
    	try {
            return Jwts.parser()
                       .setSigningKey(secretKey) // Same secret key used for signing
                       .parseClaimsJws(token)
                       .getBody();
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT signature");
        }
    }
}
