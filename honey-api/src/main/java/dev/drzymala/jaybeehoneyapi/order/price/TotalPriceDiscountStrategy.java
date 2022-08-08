package dev.drzymala.jaybeehoneyapi.order.price;

import dev.drzymala.jaybeehoneyapi.order.domain.Order;
import dev.drzymala.jaybeehoneyapi.order.domain.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class TotalPriceDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculate(Order order) {
        BigDecimal lowestPrice = lowestBookPrice(order.getItems());
        if (isGreaterOrEqual(order, 400)) {
            return lowestPrice;
        } else if (isGreaterOrEqual(order, 200)) {
            return lowestPrice.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal lowestBookPrice(Set<OrderItem> items) {
        return items.stream()
                .map(x -> x.getProduct().getPrice())
                .sorted()
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    private boolean isGreaterOrEqual(Order order, int value) {
        return order.getItemsPrice().compareTo(BigDecimal.valueOf(value)) >= 0;
    }
}
