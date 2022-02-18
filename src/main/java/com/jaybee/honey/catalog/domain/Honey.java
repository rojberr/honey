package com.jaybee.honey.catalog.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Honey {

    @Id
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer amount;
    private String coverId;


    public Honey(String productName, BigDecimal price, Integer amount) {

        this.name = productName;
        this.price = price;
        this.amount = amount;
    }
}
