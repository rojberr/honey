package com.jaybee.honey.catalog.web;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.domain.Honey;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/catalog")
@RestController
@AllArgsConstructor
public class CatalogController {

    private final CatalogUseCase catalog;


    @GetMapping
    public List<Honey> getAll() {

        return catalog.findAll();
    }

    @GetMapping(value = "/{id}")
    public Honey getById(@PathVariable Long id) {

        return catalog.findById(id).orElse(null);
    }
}
