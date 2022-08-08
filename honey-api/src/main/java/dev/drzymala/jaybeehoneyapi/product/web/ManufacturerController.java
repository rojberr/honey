package dev.drzymala.jaybeehoneyapi.product.web;

import dev.drzymala.jaybeehoneyapi.product.application.port.ManufacturerUseCase;
import dev.drzymala.jaybeehoneyapi.product.domain.Manufacturer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/manufacturer")
public class ManufacturerController {

    private final ManufacturerUseCase manufacturerUseCase;

    // GET available for everyone
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Manufacturer> findAll() {
        return manufacturerUseCase.findAll();
    }
}
