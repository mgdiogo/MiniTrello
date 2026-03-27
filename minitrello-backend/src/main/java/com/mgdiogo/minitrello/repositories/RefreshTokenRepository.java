package com.mgdiogo.minitrello.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mgdiogo.minitrello.entities.RefreshTokenEntity;
import com.mgdiogo.minitrello.entities.UserEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    List<RefreshTokenEntity> findByUser(UserEntity user);

    List<RefreshTokenEntity> findByUserAndRevokedAtIsNull(UserEntity user);

    @Query("""
            SELECT rt
            FROM RefreshTokenEntity rt
            WHERE rt.user = :user
                AND rt.revokedAt IS NULL
                AND rt.expiresAt > :now
            ORDER BY rt.createdAt ASC
            """)
    List<RefreshTokenEntity> findActiveTokensByUser(
            @Param("user") UserEntity user,
            @Param("now") LocalDateTime now);

    @Query("""
                SELECT COUNT(rt)
                FROM RefreshTokenEntity rt
                WHERE rt.user = :user
                  AND rt.revokedAt IS NULL
                  AND rt.expiresAt > :now
            """)
    long countActiveTokensByUser(
            @Param("user") UserEntity user,
            @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("""
                DELETE FROM RefreshTokenEntity rt
                WHERE rt.revokedAt IS NOT NULL
                  AND rt.revokedAt < :cutoff
            """)
    int deleteOldRevokedTokens(@Param("cutoff") LocalDateTime cutoff);

    @Modifying
    @Transactional
    @Query("""
                DELETE FROM RefreshTokenEntity rt
                WHERE rt.expiresAt < :cutoff
            """)
    int deleteExpiredTokens(@Param("cutoff") LocalDateTime cutoff);

    void deleteByUser(UserEntity user);
}
