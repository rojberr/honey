package com.jaybee.honey.order.application;

import com.jaybee.honey.order.domain.OrderItem;
import com.jaybee.honey.order.domain.OrderStatus;
import com.jaybee.honey.order.domain.Recipient;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Value
public
class RichOrder {
    Long id;
    OrderStatus status;
    Set<OrderItem> items;
    Recipient recipient;
    LocalDateTime createdAt;


    public BigDecimal totalPrice() {
        return items.stream()
                .map(item -> item.getHoney().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
