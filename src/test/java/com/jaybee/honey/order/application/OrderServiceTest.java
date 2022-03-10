package com.jaybee.honey.order.application;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.db.HoneyJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.order.application.port.QueryOrderUseCase;
import com.jaybee.honey.order.domain.OrderStatus;
import com.jaybee.honey.order.domain.Recipient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

import static com.jaybee.honey.order.application.port.ManipulateOrderUseCase.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderServiceTest {

    @Autowired
    HoneyJpaRepository honeyRepository;

    @Autowired
    ManipulateOrderService service;

    @Autowired
    CatalogUseCase catalogUseCase;

    @Autowired
    QueryOrderUseCase queryOrderService;

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
        assertEquals(40L, availableCopiesOf(honey1));
    }

    @Test
    public void userCanRevokeOrder() {
        // Given
        Honey honey1 = givenHoney1(50L);
        Long orderId = placeOrder(honey1.getId(), 40);
        assertEquals(10L, availableCopiesOf(honey1));

        // When
        service.updateOrderStatus(orderId, OrderStatus.CANCELLED);

        // Then
        assertEquals(50L, availableCopiesOf(honey1));
        assertEquals(OrderStatus.CANCELLED, queryOrderService.findById(orderId).get().getStatus());
    }

    @Test
    public void userCannotRevokePaidOrder() throws IllegalArgumentException {
        // Given
        Honey honey1 = givenHoney1(50L);
        Long orderId = placeOrder(honey1.getId(), 40);
        service.updateOrderStatus(orderId, OrderStatus.PAID);

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        });
        // Then
        assertTrue(exception.getMessage().contains("Unable to mark "
                + queryOrderService.findById(orderId).get().getStatus()
                + " order as CANCELLED"));
    }

    @Test
    public void userCannotRevokeShippedOrder() {
        // Given
        Honey honey1 = givenHoney1(50L);
        Long orderId = placeOrder(honey1.getId(), 40);
        service.updateOrderStatus(orderId, OrderStatus.PAID);
        service.updateOrderStatus(orderId, OrderStatus.SHIPPED);
        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        });
        // Then
        assertTrue(exception.getMessage().contains("Unable to mark "
                + queryOrderService.findById(orderId).get().getStatus()
                + " order as CANCELLED"));
    }

    @Test
    public void userCannotOrderNoExistingHoneys() {
        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            placeOrder(0L, 40);
        });
        // Then
        assertTrue(exception.getMessage().contains("Unable to find"));
    }

    @Test
    public void userCannotOrderMoreBooksThanAvailable() {
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

    @Disabled("Needs to be implemented")
    public void userCannotOrderNegativeNumberOfHoneys() {
        // Given
        Honey honey1 = givenHoney1(5L);
        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Long orderId = placeOrder(honey1.getId(), -5);
        });

        // Then
        assertTrue(exception.getMessage().contains("Too many products with id " + honey1.getId() + " requested: 10 available: 5 !"));
    }

    private Long placeOrder(Long honeyId, int quantity) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(honeyId, quantity))
                .build();
        return service.placeOrder(command).getRight();
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

    private Long availableCopiesOf(Honey honey1) {
        return catalogUseCase.findById(honey1.getId())
                .get().getAvailable();
    }
}