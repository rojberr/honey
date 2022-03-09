package com.jaybee.honey.catalog.web;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.domain.Honey;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogControllerApiTest {

    @LocalServerPort
    private int port;

    @MockBean
    CatalogUseCase catalogUseCase;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void getAllBooks() {
        // Given
        Honey honey1 = new Honey("Name 1", BigDecimal.valueOf(25), 99, 5L);
        Honey honey2 = new Honey("Name 2", BigDecimal.valueOf(50), 1234, 5L);
        Mockito.when(catalogUseCase.findAll()).thenReturn(List.of(honey1, honey2));
        ParameterizedTypeReference<List<Honey>> type = new ParameterizedTypeReference<>() {
        };

        // When
        String url = "http://localhost:" + port + "/catalog";
        RequestEntity<Void> request = RequestEntity.get(URI.create(url)).build();
        ResponseEntity<List<Honey>> response = restTemplate.exchange(request, type);

        // Then
        assertEquals(2, response.getBody().size());
    }
}