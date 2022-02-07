package com.jaybee.honey.catalog.application;

import com.jaybee.honey.catalog.domain.CatalogService;
import com.jaybee.honey.catalog.domain.Honey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService service;


    public List<Honey> findByName(String name) {

        return service.findByName(name);
    }

}
