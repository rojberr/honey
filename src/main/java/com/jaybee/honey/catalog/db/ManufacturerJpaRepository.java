package com.jaybee.honey.catalog.db;

import com.jaybee.honey.catalog.domain.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerJpaRepository extends JpaRepository<Manufacturer, Long> {
}
