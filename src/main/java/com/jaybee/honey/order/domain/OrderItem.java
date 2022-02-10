package com.jaybee.honey.order.domain;

import com.jaybee.honey.catalog.domain.Honey;
import lombok.Value;

@Value
public class OrderItem {

    Honey honey;
    int quantity;
}
