package dev.drzymala.jaybeehoneyapi.order.db;

import dev.drzymala.jaybeehoneyapi.order.domain.Order;
import dev.drzymala.jaybeehoneyapi.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    List<Order> findAll();

    List<Order> findByStatusAndCreatedAtLessThanEqual(OrderStatus status, LocalDateTime time);
}
