package com.mgdiogo.minitrello.security;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.mgdiogo.minitrello.configs.AuthTokenProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final AuthTokenProperties authTokenProperties;

    public String getUsername(String token) { return extractClaim(token, Claims::getSubject); }

    public String generateAccessToken(
        Map<String, Object> extraClaims,
        String sub
    ) {
        return Jwts
            .builder()
            .claims().add(extraClaims).and()
            .subject(sub)
            .issuer(authTokenProperties.getIssuer())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(authTokenProperties.getAccessTokenExpirationMinutes())))
            .signWith(getSignInKey())
            .compact();
    }

    public boolean isTokenValid(String token) {
        return (
            !isTokenExpired(token)
            && isAccessToken(token)
            && isIssuerValid(token)
            && validateRoles(token)
            && hasClaim(token, "sub", String.class)
            && hasClaim(token, "uid", Long.class)
            && hasClaim(token, "roles", List.class)
            && hasClaim(token, "iat", Date.class)
        );
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) { 
        return Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(authTokenProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String getTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    private boolean isAccessToken(String token) {
        return "access".equals(getTokenType(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private boolean isIssuerValid(String token) {
        return authTokenProperties.getIssuer().equals(extractClaim(token, Claims::getIssuer));
    }

    private <T> boolean hasClaim(String token, String claimName, Class<T> type) {
        return extractClaim(token, claims -> claims.get(claimName, type)) != null;
    }

    private boolean validateRoles(String token) {
        List<?> roles = extractClaim(token, claims -> claims.get("roles", List.class));

        if (roles == null || roles.isEmpty())
            return false;

        Set<String> validRoles = Set.of("USER", "ADMIN");

        for (Object role : roles) {
            if (role == null || !validRoles.contains(role.toString()))
                return false;   
        }

        return true;
    }
}
