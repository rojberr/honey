package com.jaybee.honey.order.price;

import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.order.domain.Order;
import com.jaybee.honey.order.domain.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceServiceTest {

    PriceService priceService = new PriceService();

    @Test
    public void calculateTotalPriceOfEmptyOrder() {
        // given
        Order order = Order.builder().build();

        // when
        OrderPrice price = priceService.calculatePrice(order);

        // then
        assertEquals(BigDecimal.ZERO, price.finalPrice());
    }

    @Test
    public void calculatesTotalPrice() {
        // given
        Honey honey1 = new Honey();
        honey1.setPrice(new BigDecimal("12.50"));
        Honey honey2 = new Honey();
        honey2.setPrice(new BigDecimal("33.99"));

        Order order = Order
                .builder()
                .item(new OrderItem(honey1, 2))
                .item(new OrderItem(honey2, 5))
                .build();

        // when
        OrderPrice price = priceService.calculatePrice(order);

        // then
        assertEquals(new BigDecimal("194.95"), price.finalPrice());
        assertEquals(new BigDecimal("194.95"), price.getItemsPrice());
    }
}