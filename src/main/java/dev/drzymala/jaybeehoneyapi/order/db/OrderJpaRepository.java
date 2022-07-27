package dev.drzymala.jaybeehoneyapi.order.db;

import dev.drzymala.jaybeehoneyapi.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    List<Order> findAll();
}
