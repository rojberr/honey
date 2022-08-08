package dev.drzymala.jaybeehoneyapi.order.application.port;

import dev.drzymala.jaybeehoneyapi.order.application.RichOrder;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {

    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);

}
