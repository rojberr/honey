package dev.drzymala.jaybeehoneyapi.product.db;

import dev.drzymala.jaybeehoneyapi.product.domain.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManufacturerJpaRepository extends JpaRepository<Manufacturer, Long> {

    Optional<Manufacturer> findByFirstNameIgnoreCase(String firsName);
}
