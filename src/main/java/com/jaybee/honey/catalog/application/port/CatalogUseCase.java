package com.jaybee.honey.catalog.application.port;

import com.jaybee.honey.catalog.domain.Honey;
import lombok.Value;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface CatalogUseCase {

    public List<Honey> findByName(String productName);

    public List<Honey> findAll();

    Optional<Honey> findOneByNameAndAmount(String productName, Integer amount);

    void addHoney(CreateHoneyCommand command);

    public void removeById();

    UpdateHoneyResponse updateHoney(UpdateHoneyCommand command);

    @Value
    class CreateHoneyCommand {

        String productName;
        long price;
        Integer amount;
    }

    @Value
    class UpdateHoneyCommand {

        Long id;
        String productName;
        long price;
        Integer amount;
    }

    @Value
    class UpdateHoneyResponse {

        public static UpdateHoneyResponse SUCCESS = new UpdateHoneyResponse(true, emptyList());
        boolean success;
        List<String> errors;
    }

}
