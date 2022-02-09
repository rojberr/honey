package com.jaybee.honey.catalog.application;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.domain.CatalogRepository;
import com.jaybee.honey.catalog.domain.Honey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class CatalogService implements CatalogUseCase {

    private final CatalogRepository repository;

    public CatalogService(@Qualifier("healthHoneyRepository") CatalogRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Honey> findByName(String productName) {

        return repository.findAll()
                .stream()
                .filter(honey -> honey.getProductName().startsWith(productName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Honey> findAll() {

        return null;
    }

    @Override
    public Optional<Honey> findByNameAndPrice(String name, long price) {

        return Optional.empty();
    }

    @Override
    public void addBook() {


    }

    @Override
    public void removeById() {

    }

    @Override
    public void updateHoney() {

    }
}
