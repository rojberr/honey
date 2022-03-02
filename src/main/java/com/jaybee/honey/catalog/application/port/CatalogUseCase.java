package com.jaybee.honey.catalog.application.port;

import com.jaybee.honey.catalog.domain.Honey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptyList;

public interface CatalogUseCase {

    public List<Honey> findAll();

    Optional<Honey> findById(Long id);

    public List<Honey> findByName(String productName);

    public Optional<Honey> findOneByName(String productName);

    List<Honey> findByManufacturers(String name);

    public Optional<Honey> findOneByPrice(Long price);

    public Optional<Honey> findOneByAmount(Integer amount);

    List<Honey> findByNameAndAmount(String productName, Integer amount);

    Optional<Honey> findOneByNameAndAmount(String productName, Integer amount);

    Honey addHoney(CreateHoneyCommand command);

    public void removeById(Long id);

    void updateHoneyCover(UpdateHoneyCoverCommand command);

    UpdateHoneyResponse updateHoney(UpdateHoneyCommand command);

    void removeHoneyCover(Long id);

    @Value
    class UpdateHoneyCoverCommand {
        Long id;
        byte[] file;
        String contentType;
        String filename;
    }

    @Value
    class CreateHoneyCommand {
        String name;
        Set<Long> manufacturers;
        BigDecimal price;
        Integer amount;
        Long available;
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateHoneyCommand {

        Long id;
        String name;
        Set<Long> manufacturers;
        BigDecimal price;
        Integer amount;
    }

    @Value
    class UpdateHoneyResponse {

        public static UpdateHoneyResponse SUCCESS = new UpdateHoneyResponse(true, emptyList());
        boolean success;
        List<String> errors;
    }

}
