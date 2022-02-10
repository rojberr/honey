package com.jaybee.honey.catalog.infrastructure;

import com.jaybee.honey.catalog.domain.CatalogRepository;
import com.jaybee.honey.catalog.domain.Honey;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HealthHoneyRepository implements CatalogRepository {

    private final Map<Long, Honey> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_VALUE = new AtomicLong(0L);


    @Override
    public List<Honey> findAll() {

        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(Honey honey) {

        if (honey.getId() != null) {
            storage.put(honey.getId(), honey);
        } else {
            long nextId = nextId();
            honey.setId(nextId);
            storage.put(nextId, honey);
        }
    }

    @Override
    public Optional<Honey> findById(Long id) {

        return Optional.ofNullable(storage.get(id));
    }

    private long nextId() {
        return ID_NEXT_VALUE.getAndIncrement();
    }
}
