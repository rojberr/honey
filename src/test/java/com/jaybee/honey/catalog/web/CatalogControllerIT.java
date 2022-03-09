package com.jaybee.honey.catalog.web;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.db.ManufacturerJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.catalog.domain.Manufacturer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.jaybee.honey.catalog.application.port.CatalogUseCase.CreateHoneyCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
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
        givenHoney1();
        givenHoney2();
        // When
        List<Honey> all = controller.getAll(Optional.empty(), Optional.empty(), Optional.of("Producent 1"), false, 1);
        // Then
        assertEquals(2, all.size());
    }

    @Test
    public void getByManufacturer() {
        // Given
        givenHoney1();
        Manufacturer manufacturer2 = manufacturerJpaRepository.save(new Manufacturer("Producent 2", "last name 1"));
        Honey honey2 = useCase.addHoney(new CreateHoneyCommand(
                "Name2",
                Set.of(manufacturer2.getId()),
                new BigDecimal("999"),
                522,
                999L));
        // When
        List<Honey> all = controller.getAll(Optional.empty(), Optional.empty(), Optional.of("Producent 2"), false, 1);
        // Then
        assertEquals(1, all.size());
        assertEquals("Name2", all.get(0).getName());
    }

    private void givenHoney1() {
        Manufacturer manufacturer1 = manufacturerJpaRepository.save(new Manufacturer("Producent 1", "last name 1"));
        Honey honey1 = useCase.addHoney(new CreateHoneyCommand(
                "Name1",
                Set.of(manufacturer1.getId()),
                new BigDecimal("200"),
                55,
                25L));
    }

    private void givenHoney2() {
        Manufacturer manufacturer2 = manufacturerJpaRepository.save(new Manufacturer("Producent 1", "last name 1"));
        Honey honey2 = useCase.addHoney(new CreateHoneyCommand(
                "Name2",
                Set.of(manufacturer2.getId()),
                new BigDecimal("999"),
                522,
                999L));
    }
}
