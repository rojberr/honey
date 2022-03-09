package com.jaybee.honey.order.application;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.db.HoneyJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.order.domain.Recipient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static com.jaybee.honey.order.application.port.ManipulateOrderUseCase.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ManipulateOrderServiceTest {

    @Autowired
    HoneyJpaRepository honeyRepository;

    @Autowired
    ManipulateOrderService service;

    @Autowired
    CatalogUseCase catalogUseCase;

    @Test
    public void userCanPlaceOrder() {
        // Given
        Honey honey1 = givenHoney1(50L);
        Honey honey2 = givenHoney2(50L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(honey1.getId(), 10))
                .item(new OrderItemCommand(honey2.getId(), 10))
                .build();

        // When
        PlaceOrderResponse response = service.placeOrder(command);

        // Then
        assertTrue(response.isSuccess());
        assertEquals(40L, catalogUseCase.findById(honey1.getId())
                .get().getAvailable());
    }

    private Honey givenHoney1(long available) {
        return honeyRepository.save(new Honey("Name 1", BigDecimal.valueOf(25), 123, available));
    }

    private Honey givenHoney2(long available) {
        return honeyRepository.save(new Honey("Name 2", BigDecimal.valueOf(50), 1233, available));
    }

    private Recipient recipient() {
        return Recipient.builder().email("client-email@email.com").build();
    }

    @Test
    public void userCantOrderMoreBooksThanAvailable() {
        // Given
        Honey honey1 = givenHoney1(5L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(honey1.getId(), 10))
                .build();

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.placeOrder(command);
        });

        // Then
        assertTrue(exception.getMessage().contains("Too many products with id " + honey1.getId() + " requested: 10 available: 5 !"));
    }
}