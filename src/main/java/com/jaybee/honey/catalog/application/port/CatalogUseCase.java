package com.jaybee.honey.catalog.application.port;

import com.jaybee.honey.catalog.domain.Honey;

import java.util.List;
import java.util.Optional;

public interface CatalogUseCase {

    public List<Honey> findByName(String productName);

    public List<Honey> findAll();

    Optional<Honey> findByNameAndPrice(String name, long price);

    public void addBook();

    public void removeById();

    void updateHoney();
}
