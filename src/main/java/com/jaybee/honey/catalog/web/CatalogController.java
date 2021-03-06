package com.jaybee.honey.catalog.web;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.application.port.CatalogUseCase.UpdateHoneyCommand;
import com.jaybee.honey.catalog.application.port.CatalogUseCase.UpdateHoneyCoverCommand;
import com.jaybee.honey.catalog.application.port.CatalogUseCase.UpdateHoneyResponse;
import com.jaybee.honey.catalog.domain.Honey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jaybee.honey.catalog.application.port.CatalogUseCase.CreateHoneyCommand;

@Slf4j
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
            @RequestParam Optional<String> manufacturerName,
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
        } else if (manufacturerName.isPresent()) {
            return catalog.findByManufacturers(manufacturerName.get())
                    .stream()
                    .collect(Collectors.toList());
        }
        return catalog.findAll().stream().limit(limit).collect(Collectors.toList());
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        // Showing how to throw exception inside of a method
        if (id.equals(42L)) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I'm a teapot. Sorry");
        }
        return catalog
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateHoney(@PathVariable Long id,
                            @RequestBody RestHoneyCommand command) {
        UpdateHoneyResponse response = catalog.updateHoney(command.toUpdateCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/{id}/cover", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addHoneyCover(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        log.info("Got file " + file.getOriginalFilename());
        catalog.updateHoneyCover(new UpdateHoneyCoverCommand(
                id,
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()
        ));
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeHoneyCover(@PathVariable Long id) {
        catalog.removeHoneyCover(id);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addHoney(@Valid @RequestBody CatalogController.RestHoneyCommand command) {
        Honey honey = catalog.addHoney(command.toCreateCommand());
        URI uri = createdHoneyURI(honey);
        return ResponseEntity.created(uri).build();
    }

    @Secured({"ROLE_ADMIN"})
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
    private static class RestHoneyCommand {
        @NotBlank
        private String name;
        @NotEmpty
        private Set<Long> manufacturers;
        @NotNull
        @DecimalMin("0.00")
        private BigDecimal price;
        @NotNull
        @DecimalMin("0.00")
        private Integer amount;
        @NotNull
        @PositiveOrZero
        private Long available;

        CreateHoneyCommand toCreateCommand() {
            return new CreateHoneyCommand(name, manufacturers, price, amount, available);
        }


        UpdateHoneyCommand toUpdateCommand(Long id) {
            return new UpdateHoneyCommand(id, name, manufacturers, price, amount);
        }
    }
}
