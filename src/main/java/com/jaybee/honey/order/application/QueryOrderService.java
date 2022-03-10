package com.jaybee.honey.order.application;

import com.jaybee.honey.order.application.port.QueryOrderUseCase;
import com.jaybee.honey.order.db.OrderJpaRepository;
import com.jaybee.honey.order.domain.Order;
import com.jaybee.honey.order.price.OrderPrice;
import com.jaybee.honey.order.price.PriceService;
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
    private final PriceService priceService;

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
        OrderPrice orderPrice = priceService.calculatePrice(order);
        return new RichOrder(
                order.getId(),
                order.getStatus(),
                order.getItems(),
                order.getRecipient(),
                order.getCreatedAt(),
                orderPrice,
                orderPrice.finlaPrice()
        );
    }
}
