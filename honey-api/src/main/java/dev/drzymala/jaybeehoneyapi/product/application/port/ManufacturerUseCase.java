package dev.drzymala.jaybeehoneyapi.product.application.port;

import dev.drzymala.jaybeehoneyapi.product.domain.Manufacturer;

import java.util.List;

public interface ManufacturerUseCase {

    List<Manufacturer> findAll();
}
