package com.jaybee.honey.catalog.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Honey {

    private Long id;
    private String productName;
    private BigDecimal price;
    private Integer amount;


    public Honey(String productName, BigDecimal price, Integer amount) {

        this.productName = productName;
        this.price = price;
        this.amount = amount;
    }
}
