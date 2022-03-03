package com.jaybee.honey.catalog.db;

import com.jaybee.honey.catalog.domain.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManufacturerJpaRepository extends JpaRepository<Manufacturer, Long> {

    Optional<Manufacturer> findByFirstNameIgnoreCase(String firsName);
}
