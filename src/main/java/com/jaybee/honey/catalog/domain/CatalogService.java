package com.jaybee.honey.catalog.domain;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final CatalogRepository repository;

    public CatalogService(CatalogRepository repository) {

        this.repository = repository;
    }

    public List<Honey> findByName(String productName) {

        return repository.findAll()
                .stream()
                .filter(honey -> honey.productName.startsWith(productName))
                .collect(Collectors.toList());
    }
}
