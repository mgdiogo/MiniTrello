package com.mgdiogo.minitrello.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import com.mgdiogo.minitrello.configs.AuthTokenProperties;
import com.mgdiogo.minitrello.entities.RefreshTokenEntity;
import com.mgdiogo.minitrello.entities.UserEntity;
import com.mgdiogo.minitrello.repositories.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthTokenProperties authTokenProperties;

    public String generateRefreshToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public String hashToken(String token) {
        try {
            if (token == null || token.isBlank())
                return null;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(token.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();

            return new String(Hex.encode(digest));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unexpected error trying to hash token ", e);
        }
    }

    public void storeRefreshToken(UserEntity user, String token) {
        LocalDateTime now = LocalDateTime.now();
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();

        refreshToken.setUser(user);
        refreshToken.setTokenHash(hashToken(token));
        refreshToken.setCreatedAt(now);
        refreshToken.setExpiresAt(now.plusDays(authTokenProperties.getRefreshTokenExpirationDays()));

        refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshTokenEntity> findValidRefreshToken(String token) {
        String hashed = hashToken(token);
        Optional<RefreshTokenEntity> storedToken = refreshTokenRepository.findByTokenHash(hashed);

        if (storedToken.isEmpty())
            return Optional.empty();

        RefreshTokenEntity refreshToken = storedToken.get();

        if (refreshToken.getRevokedAt() != null
                || !refreshToken.getExpiresAt().isAfter(LocalDateTime.now()))
            return Optional.empty();

        return Optional.of(refreshToken);
    }

    public void revokeRefreshToken(RefreshTokenEntity token) {
        token.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(token);
    }
}
