package com.jaybee.honey.order.db;

import com.jaybee.honey.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {}
