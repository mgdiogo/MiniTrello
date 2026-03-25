package com.mgdiogo.minitrello.security;

import java.util.Date;
import java.util.Map;
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
        CustomUserDetails userDetails
    ) {
        return Jwts
            .builder()
            .claims().add(extraClaims).and()
            .subject(userDetails.getUsername())
            .issuer(authTokenProperties.getIssuer())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(authTokenProperties.getAccessTokenExpirationMinutes())))
            .signWith(getSignInKey())
            .compact();
    }

    public boolean isTokenValid(String token, CustomUserDetails userDetails) {
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && isAccessToken(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
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

    public String getTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    public boolean isAccessToken(String token) {
        return "access".equals(getTokenType(token));
    }
}
