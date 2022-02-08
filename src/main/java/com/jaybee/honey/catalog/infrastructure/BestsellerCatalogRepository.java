package com.jaybee.honey.catalog.infrastructure;

import com.jaybee.honey.catalog.domain.CatalogRepository;
import com.jaybee.honey.catalog.domain.Honey;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class BestsellerCatalogRepository implements CatalogRepository {

    private final Map<Long, Honey> storage = new ConcurrentHashMap<>();


    public BestsellerCatalogRepository() {


        storage.put(2L, new Honey(2L, "Best Honey Light Honey", 125, 125));
    }

    @Override
    public List<Honey> findAll() {

        return new ArrayList<>(storage.values());
    }
}
