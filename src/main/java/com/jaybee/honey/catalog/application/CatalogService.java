package com.jaybee.honey.catalog.application;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.domain.CatalogRepository;
import com.jaybee.honey.catalog.domain.Honey;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {

    private final CatalogRepository repository;


    @Override
    public List<Honey> findByName(String productName) {

        return repository.findAll()
                .stream()
                .filter(honey -> honey.getProductName().contains(productName))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Honey> findOneByName(String productName) {

        return repository.findAll()
                .stream()
                .filter(honey -> honey.getProductName().contains(productName))
                .findFirst();
    }

    @Override
    public List<Honey> findAll() {

        return repository.findAll();
    }

    @Override
    public Optional<Honey> findOneByNameAndAmount(String productName, Integer amount) {

        return repository.findAll()
                .stream()
                .filter(honey -> honey.getProductName().contains(productName))
                .filter(honey -> honey.getAmount().equals(amount))
                .findFirst();
    }

    @Override
    public void addHoney(CreateHoneyCommand command) {

        Honey honey = command.toHoney();
        repository.save(honey);
    }

    @Override
    public void removeById(Long id) {

        repository.removeById(id);
    }

    @Override
    public UpdateHoneyResponse updateHoney(UpdateHoneyCommand command) {

        return repository
                .findById(command.getId())
                .map(honey -> {
                    Honey updatedHoney = command.updateFields(honey);
                    repository.save(updatedHoney);
                    return UpdateHoneyResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateHoneyResponse(false, Collections.singletonList("Honey not found with id: " + command.getId())));
    }
}
