package com.jaybee.honey.order.price;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class OrderPrice {
    BigDecimal itemsPrice;
    BigDecimal deliveryPrice;
    BigDecimal discounts;

    public BigDecimal finlaPrice() {
        return itemsPrice.add(deliveryPrice).subtract(discounts);
    }
}
