package com.jaybee.honey.catalog.application;

import com.jaybee.honey.catalog.application.port.ManufacturerUseCase;
import com.jaybee.honey.catalog.db.ManufacturerJpaRepository;
import com.jaybee.honey.catalog.domain.Manufacturer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManufacturerService implements ManufacturerUseCase {

    private final ManufacturerJpaRepository manufacturerRepository;

    @Override
    public List<Manufacturer> findAll() {
        return manufacturerRepository.findAll();
    }
}
