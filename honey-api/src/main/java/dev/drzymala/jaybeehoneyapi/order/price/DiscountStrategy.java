package dev.drzymala.jaybeehoneyapi.order.price;

import dev.drzymala.jaybeehoneyapi.order.domain.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {

    BigDecimal calculate(Order order);
}
