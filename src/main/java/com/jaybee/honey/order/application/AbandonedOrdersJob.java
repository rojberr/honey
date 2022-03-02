package com.jaybee.honey.order.application;

import com.jaybee.honey.order.db.OrderJpaRepository;
import com.jaybee.honey.order.domain.Order;
import com.jaybee.honey.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class AbandonedOrdersJob {

    private final OrderJpaRepository repository;
    private final ManipulateOrderService orderUseCase;

    @Transactional
    @Scheduled(fixedRate = 60_000)
    public void run() {
        LocalDateTime timestamp = LocalDateTime.now().minusDays(5);
        List<Order> orders = repository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, timestamp);
        System.out.print("Found orders to be abandoned: " + orders.size());
        orders.forEach(order -> orderUseCase.updateOrderStatus(order.getId(), OrderStatus.ABANDONED));
    }
}
