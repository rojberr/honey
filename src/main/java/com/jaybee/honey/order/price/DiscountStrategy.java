package com.jaybee.honey.order.price;

import com.jaybee.honey.order.domain.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(Order order);
}
