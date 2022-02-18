package com.jaybee.honey.catalog.db;

import com.jaybee.honey.catalog.domain.Honey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoneyJpaRepository extends JpaRepository<Honey, Long> {}
