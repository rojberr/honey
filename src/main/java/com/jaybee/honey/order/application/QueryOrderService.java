package com.jaybee.honey.order.application;

import com.jaybee.honey.catalog.db.HoneyJpaRepository;
import com.jaybee.honey.order.application.port.QueryOrderUseCase;
import com.jaybee.honey.order.db.OrderJpaRepository;
import com.jaybee.honey.order.domain.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class QueryOrderService implements QueryOrderUseCase {

    private final OrderJpaRepository orderRepository;
    private final HoneyJpaRepository honeyJpaRepository;

    @Override
    @Transactional
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
        return new RichOrder(
                order.getId(),
                order.getStatus(),
                order.getItems(),
                order.getRecipient(),
                order.getCreatedAt()
        );
    }
}
