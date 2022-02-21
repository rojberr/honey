package com.jaybee.honey.catalog.application;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.db.HoneyJpaRepository;
import com.jaybee.honey.catalog.db.ManufacturerJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.catalog.domain.Manufacturer;
import com.jaybee.honey.uploads.application.port.UploadUseCase;
import com.jaybee.honey.uploads.application.port.UploadUseCase.SaveUploadCommand;
import com.jaybee.honey.uploads.domain.Upload;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {

    private final ManufacturerJpaRepository manufacturerRepository;
    private final HoneyJpaRepository repository;
    private final UploadUseCase upload;


    @Override
    public List<Honey> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Honey> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Honey> findByName(String productName) {
        return repository.findAll()
                .stream()
                .filter(honey -> honey.getName().toLowerCase()
                        .contains(productName.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Honey> findOneByName(String productName) {
        return repository.findAll()
                .stream()
                .filter(honey -> honey.getName().toLowerCase()
                        .contains(productName.toLowerCase()))
                .findFirst();
    }

    @Override
    public List<Honey> findByManufacturer(String firstName) {
        return repository.findByManufacturers_firstNameContainsIgnoreCase(firstName);
    }

    @Override
    public Optional<Honey> findOneByPrice(Long price) {
        return repository.findAll()
                .stream()
                .filter(honey -> honey.getPrice().equals(price))
                .findFirst();
    }

    @Override
    public Optional<Honey> findOneByAmount(Integer amount) {
        return repository.findAll()
                .stream()
                .filter(honey -> honey.getAmount().equals(amount)
                ).findFirst();
    }

    @Override
    public Optional<Honey> findOneByNameAndAmount(String productName, Integer amount) {

        return repository.findAll()
                .stream()
                .filter(honey -> honey.getName().contains(productName))
                .filter(honey -> honey.getAmount().equals(amount))
                .findFirst();
    }

    @Override
    public List<Honey> findByNameAndAmount(String productName, Integer amount) {

        return repository.findAll()
                .stream()
                .filter(honey -> honey.getName().contains(productName))
                .filter(honey -> honey.getAmount().equals(amount))
                .collect(Collectors.toList());
    }

    @Override
    public Honey addHoney(CreateHoneyCommand command) {
        Honey honey = toHoney(command);
        return repository.save(honey);
    }

    private Honey toHoney(CreateHoneyCommand command) {
        Honey honey = new Honey(command.getName(), command.getPrice(), command.getAmount());
        Set<Manufacturer> manufacturerSet = fetchManufacturersByIds(command.getManufacturers());
        honey.setManufacturers(manufacturerSet);
        return honey;
    }

    private Set<Manufacturer> fetchManufacturersByIds(Set<Long> manufacturers) {
        return manufacturers
                .stream()
                .map(manufacturerId -> manufacturerRepository
                        .findById(manufacturerId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find manufacturer with id: " + manufacturerId))
                ).collect(Collectors.toSet());
    }

    private Honey updateFields(UpdateHoneyCommand command, Honey honey) {
        if (command.getName() != null) {
            honey.setName(command.getName());
        }
        if (command.getManufacturers() != null && !command.getManufacturers().isEmpty()) {
            honey.setManufacturers(fetchManufacturersByIds(command.getManufacturers()));
        }
        if (command.getAmount() != null) {
            honey.setAmount(command.getAmount());
        }
        if (command.getPrice() != null) {
            honey.setPrice(command.getPrice());
        }
        return honey;
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void updateHoneyCover(UpdateHoneyCoverCommand command) {
        int length = command.getFile().length;
        System.out.println("Received honey: " + command.getFilename()
                + " bytes: " + length);
        repository.findById(command.getId())
                .ifPresent(honey -> {
                    Upload savedUpload = upload.save(new SaveUploadCommand(command.getFilename(), command.getFile(), command.getContentType()));
                    honey.setCoverId(savedUpload.getId());
                    repository.save(honey);
                });
    }

    @Override
    public UpdateHoneyResponse updateHoney(UpdateHoneyCommand command) {
        return repository
                .findById(command.getId())
                .map(honey -> {
                    Honey updatedHoney = updateFields(command, honey);
                    repository.save(updatedHoney);
                    return UpdateHoneyResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateHoneyResponse(false, Collections.singletonList("Honey not found with id: " + command.getId())));
    }

    @Override
    public void removeHoneyCover(Long id) {
        repository.findById(id)
                .ifPresent(honey -> {
                    if (honey.getCoverId() != null) {
                        upload.removeById(honey.getCoverId());
                        honey.setCoverId(null);
                        repository.save(honey);
                    }
                });
    }
}
