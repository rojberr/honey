package com.jaybee.honey;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final Map<Long, Honey> storage = new ConcurrentHashMap<>();

    public CatalogService() {
        storage.put(1L, new Honey(1L, "Big jar", 75, 75));
        storage.put(2L, new Honey(2L, "Medium jar", 50, 50));
        storage.put(3L, new Honey(3L, "Small jar", 25, 25));
    }

    protected List<Honey> findByName(String productName) {

        return storage.values()
                .stream()
                .filter(honey -> honey.productName.startsWith(productName))
                .collect(Collectors.toList());
    }
}
