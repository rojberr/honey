package dev.drzymala.jaybeehoneyapi.order.application;

import dev.drzymala.jaybeehoneyapi.order.application.port.QueryOrderUseCase;
import dev.drzymala.jaybeehoneyapi.order.db.OrderJpaRepository;
import dev.drzymala.jaybeehoneyapi.order.domain.Order;
import dev.drzymala.jaybeehoneyapi.order.price.OrderPrice;
import dev.drzymala.jaybeehoneyapi.order.price.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {

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
    @Transactional
    public Optional<RichOrder> findById(Long id) {

        return orderRepository.findById(id).map(this::toRichOrder);
    }

    private RichOrder toRichOrder(Order order) {

        OrderPrice orderPrice = priceService.calculatePrice(order);
        return
                new RichOrder(
                order.getId(),
                order.getStatus(),
                order.getItems(),
                order.getRecipient(),
                order.getCreatedAt(),
                orderPrice,
                orderPrice.finalPrice()
        );
    }
}
