package com.mgdiogo.minitrello.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgdiogo.minitrello.entities.RefreshTokenEntity;
import com.mgdiogo.minitrello.entities.UserEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long>{
    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);
    List<RefreshTokenEntity> findByUser(UserEntity user);
    List<RefreshTokenEntity> findByUserAndRevokedAtIsNull(UserEntity user);
    void deleteByUser(UserEntity user);
}
