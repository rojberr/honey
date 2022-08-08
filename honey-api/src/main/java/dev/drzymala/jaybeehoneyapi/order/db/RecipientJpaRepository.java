package dev.drzymala.jaybeehoneyapi.order.db;

import dev.drzymala.jaybeehoneyapi.order.domain.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipientJpaRepository extends JpaRepository<Recipient, Long> {

    Optional<Recipient> findByEmailIgnoreCase(String email);
}
