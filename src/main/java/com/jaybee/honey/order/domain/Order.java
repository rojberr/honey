package com.jaybee.honey.order.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Order {

    private Long id;
    private OrderStatus status;
    private Recipient recipient;
    private List<OrderItem> items;
    private LocalDateTime createdAt;

    BigDecimal totalPrice() {

        return items.stream()
                .map(item -> item.getHoney().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
