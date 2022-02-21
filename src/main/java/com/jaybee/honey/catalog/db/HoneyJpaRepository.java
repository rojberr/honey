package com.jaybee.honey.catalog.db;

import com.jaybee.honey.catalog.domain.Honey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoneyJpaRepository extends JpaRepository<Honey, Long> {

    List<Honey> findByManufacturers_firstNameContainsIgnoreCaseOrManufacturers_lastNameContainsIgnoreCase(String firstName, String lastName);
}
