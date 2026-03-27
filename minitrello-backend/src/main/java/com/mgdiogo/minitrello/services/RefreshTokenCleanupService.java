package com.mgdiogo.minitrello.services;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mgdiogo.minitrello.repositories.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenCleanupService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanUpRefreshTokens() {
        LocalDateTime now = LocalDateTime.now();

        int deletedRevoked = refreshTokenRepository.deleteOldRevokedTokens(now.minusDays(1));
        int deletedExpired = refreshTokenRepository.deleteExpiredTokens(now.minusDays(1));

        log.info("Refresh token cleanup completed. revokedDeleted={}, expiredDeleted={}", deletedRevoked, deletedExpired);
    }
}
