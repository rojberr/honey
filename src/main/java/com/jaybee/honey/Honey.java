package com.jaybee.honey;

import lombok.AllArgsConstructor;

import java.util.StringJoiner;

@AllArgsConstructor
public class Honey {

    Long id;
    String productName;
    long price;
    Integer amount;


    @Override
    public String toString() {
        return new StringJoiner(", ", Honey.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + productName + "'")
                .add("price=" + price)
                .add("ammount=" + amount)
                .toString();
    }
}
