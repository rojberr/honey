package dev.drzymala.jaybeehoneyapi.order.price;

import dev.drzymala.jaybeehoneyapi.order.domain.Order;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PriceService {

    private List<DiscountStrategy> strategies = List.of(
            new DeliveryDiscountStrategy(),
            new TotalPriceDiscountStrategy()
    );

    @Transactional
    public OrderPrice calculatePrice(Order order) {
        return new OrderPrice(
                order.getItemsPrice(),
                order.getDeliveryPrice(),
                discounts(order)
        );
    }

    private BigDecimal discounts(Order order) {
        return strategies.stream()
                .map(strategy -> strategy.calculate(order))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
