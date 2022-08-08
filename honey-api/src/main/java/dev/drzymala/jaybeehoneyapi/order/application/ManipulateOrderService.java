package dev.drzymala.jaybeehoneyapi.order.application;

import dev.drzymala.jaybeehoneyapi.order.application.port.ManipulateOrderUseCase;
import dev.drzymala.jaybeehoneyapi.order.db.OrderJpaRepository;
import dev.drzymala.jaybeehoneyapi.order.db.RecipientJpaRepository;
import dev.drzymala.jaybeehoneyapi.order.domain.Order;
import dev.drzymala.jaybeehoneyapi.order.domain.OrderItem;
import dev.drzymala.jaybeehoneyapi.order.domain.Recipient;
import dev.drzymala.jaybeehoneyapi.order.domain.UpdateStatusResult;
import dev.drzymala.jaybeehoneyapi.product.db.ProductJpaRepository;
import dev.drzymala.jaybeehoneyapi.product.domain.Product;
import dev.drzymala.jaybeehoneyapi.security.UserSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ManipulateOrderService implements ManipulateOrderUseCase {

    private final OrderJpaRepository repository;

    private final ProductJpaRepository productJpaRepository;

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
        productJpaRepository.saveAll(reduceHoneys(items));
        return PlaceOrderResponse.success(save.getId());
    }

    private Recipient getOrCreateRecipient(Recipient recipient) {
        return recipientJpaRepository.findByEmailIgnoreCase(recipient.getEmail())
                .orElse(recipient);
    }

    private Set<Product> reduceHoneys(Set<OrderItem> items) {
        return items.stream()
                .map(item -> {
                    Product honey = item.getProduct();
                    honey.setAvailable(honey.getAvailable() - item.getQuantity());
                    return honey;
                }).collect(Collectors.toSet());
    }

    private OrderItem toOrderItem(OrderItemCommand command) {
        Product honey = productJpaRepository.getById(command.getHoneyId());
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

        return repository
                .findById(command.getOrderId())
                .map(order -> {
                    UpdateStatusResult result = order.updateStatus(command.getStatus());
                    if (result.isRevoked()) {
                        productJpaRepository.saveAll(revokeHoneys(order.getItems()));
                    }
                    repository.save(order);
                    return UpdateStatusResponse.success(order.getStatus());
                })
                .orElse(UpdateStatusResponse.failure(Error.NOT_FOUND));
    }

    private Set<Product> revokeHoneys(Set<OrderItem> items) {
        return items
                .stream()
                .map(item -> {
                    Product honey = item.getProduct();
                    honey.setAvailable(honey.getAvailable() + item.getQuantity());
                    return honey;
                }).collect(Collectors.toSet());
    }
}