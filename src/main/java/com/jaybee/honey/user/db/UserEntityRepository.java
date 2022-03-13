package com.jaybee.honey.user.db;

import com.jaybee.honey.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsernameIgnoreCase(String username);
}
