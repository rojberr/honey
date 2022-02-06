package com.jaybee.honey;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CatalogService {

    private final Map<Long, Honey> storage = new ConcurrentHashMap<>();

    protected List<Honey> findByName(String productName) {

        return storage.values()
                .stream()
                .filter(honey -> honey.productName.startsWith(productName))
                .collect(Collectors.toList());
    }
}
