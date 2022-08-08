package dev.drzymala.jaybeehoneyapi.users.db;

import dev.drzymala.jaybeehoneyapi.users.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsernameIgnoreCase(String username);
}
