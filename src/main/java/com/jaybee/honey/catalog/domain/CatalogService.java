package com.jaybee.honey.catalog.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository repository;


    public List<Honey> findByName(String productName) {

        return repository.findAll()
                .stream()
                .filter(honey -> honey.getProductName().startsWith(productName))
                .collect(Collectors.toList());
    }
}
