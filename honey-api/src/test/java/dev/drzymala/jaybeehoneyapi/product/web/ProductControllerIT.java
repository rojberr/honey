package dev.drzymala.jaybeehoneyapi.product.web;

import dev.drzymala.jaybeehoneyapi.product.application.port.ProductUseCase;
import dev.drzymala.jaybeehoneyapi.product.application.port.ProductUseCase.CreateProductCommand;
import dev.drzymala.jaybeehoneyapi.product.db.ManufacturerJpaRepository;
import dev.drzymala.jaybeehoneyapi.product.domain.Manufacturer;
import dev.drzymala.jaybeehoneyapi.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class ProductControllerIT {

    @Autowired
    ManufacturerJpaRepository manufacturerJpaRepository;

    @Autowired
    ProductUseCase products;

    @Autowired
    ProductController productController;

    @Test
    public void getAllHoneys() {
        // Given
        givenHoney1();
        givenHoney2();
        // When
        List<Product> all = productController.getAll(Optional.empty(), Optional.empty());
        // Then
        assertEquals(2, all.size());
    }

    @Test
    public void getByManufacturer() {
        // Given
        givenHoney1();
        Manufacturer manufacturer2 = manufacturerJpaRepository.save(new Manufacturer("Manufacturer 2", "last name 1"));
        Product honey2 = products.addProduct(new CreateProductCommand(
                "Name2"));
        // When
        List<Product> all = productController.getAll(Optional.empty(), Optional.empty());
        // Then
        assertEquals(2, all.size());
        assertEquals("Name1", all.get(0).getProductName());
    }

    private void givenHoney1() {
        Manufacturer manufacturer1 = manufacturerJpaRepository.save(new Manufacturer("Producent 1", "last name 1"));
        Product honey1 = products.addProduct(new CreateProductCommand(
                "Name1"));
    }

    private void givenHoney2() {
        Manufacturer manufacturer2 = manufacturerJpaRepository.save(new Manufacturer("Producent 1", "last name 1"));
        Product honey2 = products.addProduct(new CreateProductCommand(
                "Name2"));
    }
}