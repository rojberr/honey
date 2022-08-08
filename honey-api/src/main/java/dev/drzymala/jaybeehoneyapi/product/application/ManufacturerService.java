package dev.drzymala.jaybeehoneyapi.product.application;

import dev.drzymala.jaybeehoneyapi.product.application.port.ManufacturerUseCase;
import dev.drzymala.jaybeehoneyapi.product.db.ManufacturerJpaRepository;
import dev.drzymala.jaybeehoneyapi.product.domain.Manufacturer;
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
