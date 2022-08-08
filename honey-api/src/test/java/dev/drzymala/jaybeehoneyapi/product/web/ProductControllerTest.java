package dev.drzymala.jaybeehoneyapi.product.web;

import dev.drzymala.jaybeehoneyapi.product.application.port.ProductUseCase;
import dev.drzymala.jaybeehoneyapi.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ProductController.class})
class ProductControllerTest {

    @MockBean
    ProductUseCase products;

    @Autowired
    ProductController controller;

    @Test
    public void shouldGetAllHoneys() {
        // Given
        Product honey1 = new Product("Name 1");
        Product honey2 = new Product("Name 2");
        Mockito.when(products.findAll()).thenReturn(List.of(honey1, honey2));

        // When
        List<Product> all = controller.getAll(Optional.empty(), Optional.empty());

        // Then
        assertEquals(2, all.size());
    }
}