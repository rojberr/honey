package com.jaybee.honey.order.application;

import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.order.domain.OrderItem;
import com.jaybee.honey.order.domain.OrderStatus;
import com.jaybee.honey.order.domain.Recipient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class RichOrderTest {

    @Test
    public void calculateTotalPriceOfEmptyOrder() {
        // given
        RichOrder order = new RichOrder(
                1L,
                OrderStatus.NEW,
                Collections.emptySet(),
                Recipient.builder().build(),
                LocalDateTime.now()
        );
        // when
        BigDecimal price = order.totalPrice();
        // then
        Assertions.assertEquals(BigDecimal.ZERO, price);
    }


    @Test
    public void calculatesTotalPrice() {
        // given
        Honey honey1 = new Honey();
        honey1.setPrice(new BigDecimal("12.50"));
        Honey honey2 = new Honey();
        honey2.setPrice(new BigDecimal("30.20"));
        Set<OrderItem> items = new HashSet<>(
                Arrays.asList(
                        new OrderItem(honey1, 2),
                        new OrderItem(honey2, 5)
                )
        );
        RichOrder order = new RichOrder(
                1L,
                OrderStatus.NEW,
                items,
                Recipient.builder().build(),
                LocalDateTime.now()
        );
        // when
        BigDecimal price = order.totalPrice();
        // then
        Assertions.assertEquals(new BigDecimal("176.00"), price);
    }
}