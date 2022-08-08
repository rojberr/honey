package dev.drzymala.jaybeehoneyapi.product.web;

import dev.drzymala.jaybeehoneyapi.product.application.port.ProductUseCase;
import dev.drzymala.jaybeehoneyapi.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class ProductControllerApiTest {

    @LocalServerPort
    private int port;

    @MockBean
    ProductUseCase products;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void getAllProducts() {
        // Given
        Product product1 = new Product("Name 1");
        Product product2 = new Product("Name 2");
        Mockito.when(products.findAll()).thenReturn(List.of(product1, product2));
        ParameterizedTypeReference<List<Product>> type = new ParameterizedTypeReference<>() {
        };

        // When
        String url = "http://localhost:" + port + "/product";
        RequestEntity<Void> request = RequestEntity.get(URI.create(url)).build();
        ResponseEntity<List<Product>> response = restTemplate.exchange(request, type);

        // Then
        assertEquals(2, response.getBody().size());
    }
}