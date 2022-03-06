package com.jaybee.honey.order.application.port;

import com.jaybee.honey.order.application.RichOrder;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {

    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);

}
