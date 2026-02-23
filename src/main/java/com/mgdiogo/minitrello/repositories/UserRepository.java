package com.mgdiogo.minitrello.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mgdiogo.minitrello.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByEmail(String email);
}
