package com.jaybee.honey.order.application;

import com.jaybee.honey.catalog.db.HoneyJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase;
import com.jaybee.honey.order.db.OrderJpaRepository;
import com.jaybee.honey.order.db.RecipientJpaRepository;
import com.jaybee.honey.order.domain.Order;
import com.jaybee.honey.order.domain.OrderItem;
import com.jaybee.honey.order.domain.OrderStatus;
import com.jaybee.honey.order.domain.Recipient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
class ManipulateOrderService implements ManipulateOrderUseCase {

    private final OrderJpaRepository repository;
    private final HoneyJpaRepository honeyJpaRepository;
    private final RecipientJpaRepository recipientJpaRepository;

    @Override
    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Set<OrderItem> items = command.getItems()
                .stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());
        Order order = Order
                .builder()
                .recipient(getOrCreateRecipient(command.getRecipient()))
                .items(items)
                .build();
        Order save = repository.save(order);
        honeyJpaRepository.saveAll(updateHoneys(items));
        return PlaceOrderResponse.success(save.getId());
    }

    private Recipient getOrCreateRecipient(Recipient recipient) {
        recipientJpaRepository.findByEmailIgnoreCase(recipient
                .getEmail())
                .orElse(recipient);
        return recipient;
    }

    private Set<Honey> updateHoneys(Set<OrderItem> items) {
        return items.stream()
                .map(item -> {
                    Honey honey = item.getHoney();
                    honey.setAvailable(honey.getAvailable() - item.getQuantity());
                    return honey;
                }).collect(Collectors.toSet());
    }

    private OrderItem toOrderItem(OrderItemCommand command) {
        Honey honey = honeyJpaRepository.getById(command.getHoneyId());
        int quantity = command.getQuantity();
        Long available = honey.getAvailable();
        if (quantity <= available) {
            return new OrderItem(honey, command.getQuantity());
        }
        throw new IllegalArgumentException("Too many products with id " + honey.getId()
                + " requested: " + quantity
                + " available: " + available + " !");
    }

    @Override
    @Transactional
    public void deleteOrderById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long id, OrderStatus status) {
        repository.findById(id)
                .ifPresent(order -> {
                    order.updateStatus(status);
//                    order.setStatus(status);
                    repository.save(order);
                });
    }
}
