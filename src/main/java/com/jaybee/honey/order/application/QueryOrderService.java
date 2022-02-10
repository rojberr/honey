package com.jaybee.honey.order.application;

import com.jaybee.honey.order.application.port.QueryOrderUseCase;
import com.jaybee.honey.order.domain.Order;
import com.jaybee.honey.order.domain.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {

    private final OrderRepository repository;

    @Override

    public List<Order> findAll() {
        return repository.findAll();
    }
}
