package com.jaybee.honey.order.application;

import com.jaybee.honey.order.db.OrderJpaRepository;
import com.jaybee.honey.order.domain.Order;
import com.jaybee.honey.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.jaybee.honey.order.application.port.ManipulateOrderUseCase.UpdateStatusCommand;

@Slf4j
@Component
@AllArgsConstructor
public class AbandonedOrdersJob {

    private final OrderJpaRepository repository;
    private final ManipulateOrderService orderUseCase;
    private final OrderProperties properties;

    @Transactional
    @Scheduled(cron = "${app.order.abandon-cron}")
    public void run() {
        Duration paymentPeriod = properties.getPaymentPeriod();
        LocalDateTime olderThan = LocalDateTime.now().minus(paymentPeriod);
        List<Order> orders = repository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, olderThan);
        log.info("Found orders to be abandoned: " + orders.size());
        orders.forEach(order -> {
            String adminEmail = "admin@test.test";
            orderUseCase.updateOrderStatus(new UpdateStatusCommand(order.getId(), OrderStatus.ABANDONED, adminEmail));
        });
    }
}