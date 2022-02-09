package com.jaybee.honey.catalog.domain;

import java.util.List;

public interface CatalogRepository {

    List<Honey> findAll();

    void save(Honey honey);
}
