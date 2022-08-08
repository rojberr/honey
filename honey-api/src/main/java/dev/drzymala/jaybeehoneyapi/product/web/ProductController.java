package dev.drzymala.jaybeehoneyapi.product.web;

import dev.drzymala.jaybeehoneyapi.product.application.port.ProductUseCase;
import dev.drzymala.jaybeehoneyapi.product.application.port.ProductUseCase.CreateProductCommand;
import dev.drzymala.jaybeehoneyapi.product.application.port.ProductUseCase.UpdateProductCommand;
import dev.drzymala.jaybeehoneyapi.product.application.port.ProductUseCase.UpdateProductResponse;
import dev.drzymala.jaybeehoneyapi.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(value = "/product")
@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductUseCase products;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAll(
            @RequestParam Optional<String> productName,
            @RequestParam Optional<String> manufacturerName
    ) {

        if (productName.isPresent()) {
            return products.findByProductName(productName.get());
        }
        if (manufacturerName.isPresent()) {
            return products.findByManufacturerName(manufacturerName.get());
        }
        return products.findAll().stream().limit(5).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        // Example - Send 42 to show how to throw exception inside a method
        if (id.equals(42L)) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I'm a teapot. Sorry");
        }
        return products
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/one")
    public ResponseEntity<?> getOneByName(@RequestParam String productName) {

        return products
                .findOneByProductName(productName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateHoney(@PathVariable Long id,
                            @RequestBody RestProductCommand command) {
        UpdateProductResponse response = products.updateProduct(command.toUpdateCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> addProduct(@Valid @RequestBody RestProductCommand command) {
        Product product = products.addProduct(command.toCreateCommand());
        return ResponseEntity.ok(product.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {

        products.removeById(id);
    }

    @Data
    private static class RestProductCommand {
        @NotBlank
        private String name;

        CreateProductCommand toCreateCommand() {
            return new CreateProductCommand(name);
        }

        UpdateProductCommand toUpdateCommand(Long id) {
            return new UpdateProductCommand(id, name);
        }
    }
}
