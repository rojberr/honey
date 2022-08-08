package dev.drzymala.jaybeehoneyapi.order.price;

import dev.drzymala.jaybeehoneyapi.order.domain.Order;

import java.math.BigDecimal;

public class DeliveryDiscountStrategy implements DiscountStrategy {

    BigDecimal THRESHOLD = new BigDecimal(100);

    @Override
    public BigDecimal calculate(Order order) {
        if (order.getItemsPrice().compareTo(THRESHOLD) >= 0) {
            return order.getDeliveryPrice();
        }
        return BigDecimal.ZERO;
    }
}
