package org.guccikray.creditcardmanagesystem.util;

import io.jsonwebtoken.Jwts;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public final class JwtUtil {

    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    public static String generateToken(@NotNull Long userId) {
        return Jwts.builder()
            .subject(String.valueOf(userId))
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 10000 * 60 * 24))
            .signWith(key)
            .compact();
    }

    public static boolean validateToken(
        @NotNull String token,
        @NotNull Long userId
    ) {
        Long tokenUserId = Long.parseLong(extractUserId(token));
        return (userId.equals(tokenUserId) && !isTokenExpired(token));
    }

    public static boolean isTokenExpired(@NotNull String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration()
            .before(new Date());
    }

    public static String extractUserId(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
