package com.jaybee.honey.catalog.application.port;

import com.jaybee.honey.catalog.domain.Honey;
import lombok.Value;

import java.util.List;
import java.util.Optional;

public interface CatalogUseCase {

    public List<Honey> findByName(String productName);

    public List<Honey> findAll();

    Optional<Honey> findByNameAndPrice(String name, long price);

    void addHoney(CreateHoneyCommand command);

    public void removeById();

    void updateHoney();

    @Value
    class CreateHoneyCommand {

        String productName;
        long price;
        Integer amount;
    }
}
