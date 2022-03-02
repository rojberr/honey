package com.jaybee.honey.catalog.web;

import com.jaybee.honey.catalog.application.port.ManufacturerUseCase;
import com.jaybee.honey.catalog.domain.Manufacturer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/manufacturers")
public class ManufacturersController {

    private final ManufacturerUseCase manufacturerUseCase;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Manufacturer> getManufacturers() {
        return manufacturerUseCase.findAll();
    }
}
