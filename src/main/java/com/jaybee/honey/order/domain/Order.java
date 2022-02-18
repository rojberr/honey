package com.jaybee.honey.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Builder.Default
    private OrderStatus status = OrderStatus.NEW;

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    private transient Recipient recipient;

    private LocalDateTime createdAt;

}
