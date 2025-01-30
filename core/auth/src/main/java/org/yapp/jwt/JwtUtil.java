package org.yapp.jwt;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
            Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getOauthId(String token) {
        String oauthId;
        try {
            oauthId = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().get("oauthId", String.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return oauthId;
    }

    public String getRole(String token) {
        String role;
        try {
            role = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("role", String.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return role;
    }

    public Boolean isExpired(String token) {
        boolean before;
        try {
            before = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return before;
    }

    public String createJwt(String category, Long userId, String oauthId, String role,
        Long expiredMs) {
        return Jwts.builder()
            .claim("category", category)
            .claim("userId", userId)
            .claim("oauthId", oauthId)
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
    }

    public Long getUserId(String token) {
        Long userId;
        try {
            userId = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().get("userId", Long.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return userId;
    }

    public String getCategory(String token) {
        String category;
        try {
            category = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().get("category", String.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return category;
    }
}

