package com.jaybee.honey.order.application.port;

import com.jaybee.honey.order.domain.Order;

import java.util.List;

public interface QueryOrderUseCase {

    List<Order> findAll();
}
