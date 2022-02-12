package com.jaybee.honey.catalog.web;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.domain.Honey;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jaybee.honey.catalog.application.port.CatalogUseCase.CreateHoneyCommand;

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

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        return catalog
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addHoney(@RequestBody RestCreateBookCommand command) {
        Honey honey = catalog.addHoney(command.toCommand());
        URI uri = createdHoneyURI(honey);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        catalog.removeById(id);
    }

    private URI createdHoneyURI(Honey honey) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/" + honey.getId().toString()).build().toUri();
    }

    @Data
    private static class RestCreateBookCommand {
        private String name;
        private BigDecimal price;
        private Integer amount;

        CreateHoneyCommand toCommand() {
            return new CreateHoneyCommand(name, price, amount);
        }
    }

}
