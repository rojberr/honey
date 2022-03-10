package com.jaybee.honey.order.application;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.db.HoneyJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.clock.Clock;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase;
import com.jaybee.honey.order.domain.OrderStatus;
import com.jaybee.honey.order.domain.Recipient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        properties = "app.order.payment-period=1H"
)
@AutoConfigureTestDatabase
class AbandonedOrdersJobTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public Clock.Fake clock() {
            return new Clock.Fake();
        }
    }

    @Autowired
    CatalogUseCase catalogUseCase;

    @Autowired
    QueryOrderService queryOrderService;

    @Autowired
    HoneyJpaRepository honeyRepository;

    @Autowired
    AbandonedOrdersJob ordersJob;

    @Autowired
    ManipulateOrderService manipulateOrderService;

    @Autowired
    Clock.Fake clock;

    private final String userEmail = "user@test.test";
    private final String adminEmail = "admin@test.test";

    @Test
    public void shouldMarkOrdersAsAbandoned() {
        // Given . orders
        Honey honey = givenHoney1(50L);
        Long orderId = placeOrder(honey.getId(), 10, userEmail);

        // When - run
        clock.tick(Duration.ofHours(2));
        ordersJob.run();

        // Then - status changed
        assertEquals(OrderStatus.ABANDONED, queryOrderService.findById(orderId).get().getStatus());
        assertEquals(50L, availableCopiesOf(honey));
    }

    private Honey givenHoney1(long available) {
        return honeyRepository.save(new Honey("Name 1", BigDecimal.valueOf(25), 123, available));
    }

    private Long placeOrder(Long honeyId, int quantity, String email) {
        ManipulateOrderUseCase.PlaceOrderCommand command = ManipulateOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient(email))
                .item(new ManipulateOrderUseCase.OrderItemCommand(honeyId, quantity))
                .build();
        return manipulateOrderService.placeOrder(command).getRight();
    }

    private Recipient recipient(String email) {
        return Recipient.builder()
                .email(email)
                .build();
    }

    private Long availableCopiesOf(Honey honey1) {
        return catalogUseCase.findById(honey1.getId())
                .get().getAvailable();
    }
}