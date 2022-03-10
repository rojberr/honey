package com.jaybee.honey.order.application;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.db.HoneyJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.order.application.port.QueryOrderUseCase;
import com.jaybee.honey.order.domain.OrderStatus;
import com.jaybee.honey.order.domain.Recipient;
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

    String userEmail = "user@test.test";
    String adminEmail = "admin@test.test";

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
        Long orderId = placeOrder(honey1.getId(), 40, userEmail);
        assertEquals(10L, availableCopiesOf(honey1));

        // When
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELLED, userEmail);
        service.updateOrderStatus(command);

        // Then
        assertEquals(50L, availableCopiesOf(honey1));
        assertEquals(OrderStatus.CANCELLED, queryOrderService.findById(orderId).get().getStatus());
    }

    @Test
    public void userCannotRevokePaidOrder() throws IllegalArgumentException {
        // Given
        Honey honey1 = givenHoney1(50L);
        Long orderId = placeOrder(honey1.getId(), 40, userEmail);
        service.updateOrderStatus(new UpdateStatusCommand(orderId, OrderStatus.PAID, userEmail));

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.updateOrderStatus(new UpdateStatusCommand(orderId, OrderStatus.CANCELLED, userEmail));
        });

        // Then
        assertEquals(10L, availableCopiesOf(honey1));
        assertTrue(exception.getMessage().contains("Unable to mark "
                + queryOrderService.findById(orderId).get().getStatus() + " order as CANCELLED"));
    }

    @Test
    public void userCannotRevokeShippedOrder() {
        // Given
        Honey honey1 = givenHoney1(50L);
        Long orderId = placeOrder(honey1.getId(), 40, userEmail);
        service.updateOrderStatus(new UpdateStatusCommand(orderId, OrderStatus.PAID, userEmail));
        service.updateOrderStatus(new UpdateStatusCommand(orderId, OrderStatus.SHIPPED, userEmail));
        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.updateOrderStatus(new UpdateStatusCommand(orderId, OrderStatus.CANCELLED, userEmail));

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

    @Test
    public void userCannotOrderNegativeNumberOfHoneys() {
        // Given
        Honey honey1 = givenHoney1(5L);
        // When
        Exception exception = assertThrows(Exception.class, () -> {
            Long orderId = placeOrder(honey1.getId(), -5);
        });
        // Then
        assertTrue(exception.getMessage().contains("The ordered quantity cannot be equal or less than zero"));
    }

    @Test
    public void userCannotRevokeOtherUsersOrder() {
        // Given
        Honey honey1 = givenHoney1(50L);
        Long orderId = placeOrder(honey1.getId(), 40, userEmail);
        assertEquals(10L, availableCopiesOf(honey1));

        // When
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELLED, "other@user.com");
        service.updateOrderStatus(command);

        // Then
        assertEquals(10L, availableCopiesOf(honey1));
        assertEquals(OrderStatus.NEW, queryOrderService.findById(orderId).get().getStatus());
    }

    @Test
    public void adminCanRevokeUsersOrder() {
        // Given
        Honey honey1 = givenHoney1(50L);
        Long orderId = placeOrder(honey1.getId(), 40, userEmail);
        assertEquals(10L, availableCopiesOf(honey1));

        // When
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELLED, adminEmail);
        service.updateOrderStatus(command);

        // Then
        assertEquals(50L, availableCopiesOf(honey1));
        assertEquals(OrderStatus.CANCELLED, queryOrderService.findById(orderId).get().getStatus());
    }


    @Test
    public void adminCanMarkOrderAsPaid() {
        // Given
        Honey honey1 = givenHoney1(50L);
        Long orderId = placeOrder(honey1.getId(), 40, userEmail);
        assertEquals(10L, availableCopiesOf(honey1));

        // When
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.PAID, adminEmail);
        service.updateOrderStatus(command);

        // Then
        assertEquals(10L, availableCopiesOf(honey1));
        assertEquals(OrderStatus.PAID, queryOrderService.findById(orderId).get().getStatus());
    }

    private Long placeOrder(Long honeyId, int quantity, String email) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient(email))
                .item(new OrderItemCommand(honeyId, quantity))
                .build();
        return service.placeOrder(command).getRight();
    }

    private Long placeOrder(Long honeyId, int quantity) {
        return placeOrder(honeyId, quantity, "example-client-email@email.com");
    }

    private Honey givenHoney1(long available) {
        return honeyRepository.save(new Honey("Name 1", BigDecimal.valueOf(25), 123, available));
    }

    private Honey givenHoney2(long available) {
        return honeyRepository.save(new Honey("Name 2", BigDecimal.valueOf(50), 1233, available));
    }

    private Recipient recipient() {
        return Recipient.builder()
                .email("example-client-email@email.com")
                .build();
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