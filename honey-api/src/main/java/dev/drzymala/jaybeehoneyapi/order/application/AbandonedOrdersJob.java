package dev.drzymala.jaybeehoneyapi.order.application;

import dev.drzymala.jaybeehoneyapi.clock.Clock;
import dev.drzymala.jaybeehoneyapi.order.db.OrderJpaRepository;
import dev.drzymala.jaybeehoneyapi.order.domain.Order;
import dev.drzymala.jaybeehoneyapi.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static dev.drzymala.jaybeehoneyapi.order.application.port.ManipulateOrderUseCase.UpdateStatusCommand;
import static dev.drzymala.jaybeehoneyapi.order.application.port.ManipulateOrderUseCase.UpdateStatusResponse;

@Slf4j
@Component
@AllArgsConstructor
public class AbandonedOrdersJob {

    private final OrderJpaRepository repository;

    private final ManipulateOrderService orderUseCase;

    private final OrderProperties properties;

    private final Clock clock;

    @Transactional
    @Scheduled(cron = "${app.order.abandon-cron}")
    public void run() {

        Duration paymentPeriod = properties.getPaymentPeriod();
        LocalDateTime olderThan = clock.now().minus(paymentPeriod);
        List<Order> orders = repository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, olderThan);

        log.info("Found orders to be abandoned: " + orders.size());

        orders.forEach(order -> {
            UpdateStatusCommand command = new UpdateStatusCommand(order.getId(), OrderStatus.ABANDONED);
            orderUseCase.updateOrderStatus(command);
        });
    }
}