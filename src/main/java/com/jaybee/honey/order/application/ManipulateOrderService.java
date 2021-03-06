package com.jaybee.honey.order.application;

import com.jaybee.honey.catalog.db.HoneyJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase;
import com.jaybee.honey.order.db.OrderJpaRepository;
import com.jaybee.honey.order.db.RecipientJpaRepository;
import com.jaybee.honey.order.domain.Order;
import com.jaybee.honey.order.domain.OrderItem;
import com.jaybee.honey.order.domain.Recipient;
import com.jaybee.honey.order.domain.UpdateStatusResult;
import com.jaybee.honey.security.UserSecurity;
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
    private final UserSecurity userSecurity;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Set<OrderItem> items = command.getItems()
                .stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());
        Order order = Order
                .builder()
                .recipient(getOrCreateRecipient(command.getRecipient()))
                .delivery(command.getDelivery())
                .items(items)
                .build();
        Order save = repository.save(order);
        honeyJpaRepository.saveAll(reduceHoneys(items));
        return PlaceOrderResponse.success(save.getId());
    }

    private Recipient getOrCreateRecipient(Recipient recipient) {
        return recipientJpaRepository.findByEmailIgnoreCase(recipient.getEmail())
                .orElse(recipient);
    }

    private Set<Honey> reduceHoneys(Set<OrderItem> items) {
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
    public void deleteOrderById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public UpdateStatusResponse updateOrderStatus(UpdateStatusCommand command) {
        return repository.findById(command.getOrderId())
                .map(order -> {
                    if (userSecurity.isOwnerOrAdmin(order.getRecipient().getEmail(), command.getUser())) {
                        UpdateStatusResult result = order.updateStatus(command.getStatus());
                        if (result.isRevoked()) {
                            honeyJpaRepository.saveAll(revokeHoneys(order.getItems()));
                        }
                        repository.save(order);
                        return UpdateStatusResponse.success(order.getStatus());
                    }
                    return UpdateStatusResponse.failure(Error.FORBIDDEN);
                })
                .orElse(UpdateStatusResponse.failure(Error.NOT_FOUND));
    }

    private Set<Honey> revokeHoneys(Set<OrderItem> items) {
        return items
                .stream()
                .map(item -> {
                    Honey honey = item.getHoney();
                    honey.setAvailable(honey.getAvailable() + item.getQuantity());
                    return honey;
                }).collect(Collectors.toSet());
    }
}
