package com.jaybee.honey.catalog.web;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.domain.Honey;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping(value = "/catalog")
@RestController
@AllArgsConstructor
public class CatalogController {

    private final CatalogUseCase catalog;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Honey> getAll(
            @RequestParam Optional<String> name,
            @RequestParam Optional<Integer> amount,
            @RequestParam(defaultValue = "false") boolean debug,
            @RequestParam(defaultValue = "10") int limit
    ) {
        if (name.isPresent() && amount.isPresent()) {
            return catalog.findByNameAndAmount(name.get(), amount.get());
        } else if (name.isPresent()) {
            return catalog.findByName(name.get());
        } else if (amount.isPresent()) {
            return catalog.findOneByAmount(amount.get())
                    .stream()
                    .collect(Collectors.toList());
        }
        return catalog.findAll().stream().limit(limit).collect(Collectors.toList());
    }

    @GetMapping(params = {"name"})
    public List<Honey> getAllFiltered(
            @RequestParam Optional<String> name,
            @RequestParam Optional<Long> id) {
        return null;
//        return catalog.findByName(name);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        return catalog
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
