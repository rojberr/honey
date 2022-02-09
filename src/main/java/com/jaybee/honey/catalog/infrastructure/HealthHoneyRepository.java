package com.jaybee.honey.catalog.infrastructure;

import com.jaybee.honey.catalog.domain.CatalogRepository;
import com.jaybee.honey.catalog.domain.Honey;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class HealthHoneyRepository implements CatalogRepository {

    private final Map<Long, Honey> storage = new ConcurrentHashMap<>();


    public HealthHoneyRepository() {

        storage.put(1L, new Honey(1L, "Big jar", 75, 75));
        storage.put(2L, new Honey(2L, "Medium jar", 50, 50));
        storage.put(3L, new Honey(3L, "Small jar", 25, 25));
    }

    @Override
    public List<Honey> findAll() {

        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(Honey honey) {

    }
}
