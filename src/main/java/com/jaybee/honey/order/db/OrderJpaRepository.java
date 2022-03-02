package com.jaybee.honey.order.db;

import com.jaybee.honey.order.domain.Order;
import com.jaybee.honey.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusAndCreatedAtLessThanEqual(OrderStatus status, LocalDateTime time);
}
