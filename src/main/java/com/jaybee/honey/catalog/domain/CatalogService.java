package com.jaybee.honey.catalog.domain;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final CatalogRepository repository;

    public CatalogService(@Qualifier("healthHoneyRepository") CatalogRepository repository) {
        this.repository = repository;
    }

    public List<Honey> findByName(String productName) {

        return repository.findAll()
                .stream()
                .filter(honey -> honey.getProductName().startsWith(productName))
                .collect(Collectors.toList());
    }
}
