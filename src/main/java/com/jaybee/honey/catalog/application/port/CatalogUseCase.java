package com.jaybee.honey.catalog.application.port;

import com.jaybee.honey.catalog.domain.Honey;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface CatalogUseCase {

    public List<Honey> findByName(String productName);

    public List<Honey> findAll();

    Optional<Honey> findOneByNameAndAmount(String productName, Integer amount);

    void addHoney(CreateHoneyCommand command);

    public void removeById(Long id);


    UpdateHoneyResponse updateHoney(UpdateHoneyCommand command);

    @Value
    class CreateHoneyCommand {

        String productName;
        BigDecimal price;
        Integer amount;
    }

    @Value
    @Builder
    class UpdateHoneyCommand {

        Long id;
        String productName;
        BigDecimal price;
        Integer amount;

        public Honey updateFields(Honey honey) {
            if (productName != null) {
                honey.setProductName(productName);
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
