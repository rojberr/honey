package com.jaybee.honey.catalog.web;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.db.ManufacturerJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.catalog.domain.Manufacturer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.jaybee.honey.catalog.application.port.CatalogUseCase.CreateHoneyCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class CatalogControllerIT {

    @Autowired
    ManufacturerJpaRepository manufacturerJpaRepository;

    @Autowired
    CatalogUseCase useCase;

    @Autowired
    CatalogController controller;

    @Test
    public void getAllHoneys() {
        // Given
        Manufacturer manufacturer1 = manufacturerJpaRepository.save(new Manufacturer("Producent 1", "last name 1"));
        Manufacturer manufacturer2 = manufacturerJpaRepository.save(new Manufacturer("Producent 1", "last name 1"));
        Honey honey1 = useCase.addHoney(new CreateHoneyCommand(
                "Name1",
                Set.of(manufacturer1.getId()),
                new BigDecimal("200"),
                55,
                25L));
        Honey honey2 = useCase.addHoney(new CreateHoneyCommand(
                "Name2",
                Set.of(manufacturer2.getId()),
                new BigDecimal("999"),
                522,
                999L));
        // When
        List<Honey> all = controller.getAll(Optional.empty(), Optional.empty(), Optional.of("Producent 1"), false, 1);
        // Then
        assertEquals(2, all.size());
    }

}