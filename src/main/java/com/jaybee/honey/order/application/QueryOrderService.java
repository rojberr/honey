package com.jaybee.honey.order.application;

import com.jaybee.honey.catalog.db.HoneyJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.order.application.port.QueryOrderUseCase;
import com.jaybee.honey.order.db.OrderJpaRepository;
import com.jaybee.honey.order.domain.Order;
import com.jaybee.honey.order.domain.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class QueryOrderService implements QueryOrderUseCase {

    private final OrderJpaRepository orderRepository;
    private final HoneyJpaRepository honeyJpaRepository;

    @Override
    public List<RichOrder> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toRichOrder)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RichOrder> findById(Long id) {
        return orderRepository.findById(id).map(this::toRichOrder);
    }

    private RichOrder toRichOrder(Order order) {
        List<RichOrderItem> richItems = toRichItems(order.getItems());
        return new RichOrder(
                order.getId(),
                order.getStatus(),
                richItems,
                order.getRecipient(),
                order.getCreatedAt()
        );
    }

    private List<RichOrderItem> toRichItems(List<OrderItem> items) {
        return items.stream()
                .map(item -> {
                    Honey honey = honeyJpaRepository
                            .findById(item.getHoneyId())
                            .orElseThrow(() -> new IllegalStateException("Unable to find honey with ID: " + item.getHoneyId()));
                    return new RichOrderItem(honey, item.getQuantity());
                })
                .collect(Collectors.toList());
    }
}
