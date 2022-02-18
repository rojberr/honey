package com.jaybee.honey.order.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private Long honeyId;

    int quantity;

    public OrderItem(Long honeyId, int quantity) {
        this.honeyId = honeyId;
        this.quantity = quantity;
    }
}

