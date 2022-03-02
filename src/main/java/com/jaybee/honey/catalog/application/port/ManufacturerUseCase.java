package com.jaybee.honey.catalog.application.port;

import com.jaybee.honey.catalog.domain.Manufacturer;

import java.util.List;

public interface ManufacturerUseCase {

    List<Manufacturer> findAll();
}
