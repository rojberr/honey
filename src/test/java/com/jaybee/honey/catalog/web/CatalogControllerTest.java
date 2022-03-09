package com.jaybee.honey.catalog.web;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.domain.Honey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CatalogController.class})
class CatalogControllerTest {

    @MockBean
    CatalogUseCase catalogUseCase;

    @Autowired
    CatalogController controller;

    @Test
    public void shouldGetAllHoneys() {
        // Given
        Honey honey1 = new Honey("Name 1", BigDecimal.valueOf(25), 99, 5L);
        Honey honey2 = new Honey("Name 2", BigDecimal.valueOf(50), 1234, 5L);
        Mockito.when(catalogUseCase.findAll()).thenReturn(List.of(honey1, honey2));

        // When
        List<Honey> all = controller.getAll(Optional.empty(), Optional.empty(), Optional.empty(), true, 5);

        // Then
        assertEquals(2, all.size());
    }
}