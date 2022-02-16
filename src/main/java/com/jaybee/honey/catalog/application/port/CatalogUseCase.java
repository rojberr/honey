package com.jaybee.honey.catalog.application.port;

import com.jaybee.honey.catalog.domain.Honey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface CatalogUseCase {

    public List<Honey> findAll();

    Optional<Honey> findById(Long id);

    public List<Honey> findByName(String productName);

    public Optional<Honey> findOneByName(String productName);

    public Optional<Honey> findOneByPrice(Long price);

    public Optional<Honey> findOneByAmount(Integer amount);

    List<Honey> findByNameAndAmount(String productName, Integer amount);

    Optional<Honey> findOneByNameAndAmount(String productName, Integer amount);

    Honey addHoney(CreateHoneyCommand command);

    public void removeById(Long id);

    void updateHoneyCover(UpdateHoneyCoverCommand command);

    UpdateHoneyResponse updateHoney(UpdateHoneyCommand command);

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
        BigDecimal price;
        Integer amount;

        public Honey toHoney() {
            return new Honey(name, price, amount);
        }
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateHoneyCommand {

        Long id;
        String name;
        BigDecimal price;
        Integer amount;

        public Honey updateFields(Honey honey) {
            if (name != null) {
                honey.setName(name);
            }
            if (price != null) {
                honey.setPrice(price);
            }
            if (amount != null) {
                honey.setAmount(amount);
            }
            return honey;
        }
    }

    @Value
    class UpdateHoneyResponse {

        public static UpdateHoneyResponse SUCCESS = new UpdateHoneyResponse(true, emptyList());
        boolean success;
        List<String> errors;
    }

}
