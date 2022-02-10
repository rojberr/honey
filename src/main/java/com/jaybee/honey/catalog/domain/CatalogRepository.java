package com.jaybee.honey.catalog.domain;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository {

    List<Honey> findAll();

    void save(Honey honey);

    Optional<Honey> findById(Long id);

    void removeById(Long id);
}
