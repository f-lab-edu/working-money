package org.example.workingmoney.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AuthTokenUtil {

    @NotNull
    private SecretKey secretKey;

    public AuthTokenUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {

        return getClaim(token, ClaimNameConstants.USERNAME);
    }

    public String getRole(String token) {

        return getClaim(token, ClaimNameConstants.ROLE);
    }

    public Optional<JwtType> getCategory(String token) {

        String category = getClaim(token, ClaimNameConstants.CATEGORY);
        return JwtType.makeJwtType(category);
    }

    public Boolean isExpired(String token) {
        try {
            getPayload(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public String createJwt(JwtType type, String username, String role) {
        return Jwts.builder()
                .claim(ClaimNameConstants.CATEGORY.getValue(), type.getCategoryName())
                .claim(ClaimNameConstants.USERNAME.getValue(), username)
                .claim(ClaimNameConstants.ROLE.getValue(), role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + type.getExpirationTime()))
                .signWith(secretKey)
                .compact();
    }

    private Claims getPayload(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    private String getClaim(String token, ClaimNameConstants claimNameConstants) {
        return getPayload(token).get(claimNameConstants.getValue(), String.class);
    }
}

