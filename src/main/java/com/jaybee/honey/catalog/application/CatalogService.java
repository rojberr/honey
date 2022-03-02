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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {

    private final ManufacturerJpaRepository manufacturerRepository;
    private final HoneyJpaRepository repository;
    private final UploadUseCase upload;

    @Override
    public List<Honey> findAll() {
        return repository.findAllEager();
    }

    @Override
    public Optional<Honey> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Honey> findByName(String productName) {
        return repository.findByNameStartsWithIgnoreCase(productName);
    }

    @Override
    public Optional<Honey> findOneByName(String productName) {
        return repository.findDistinctFirstByNameStartsWithIgnoreCase(productName);
    }

    @Override
    public List<Honey> findByManufacturers(String name) {
        return repository
                .findByManufacturer(name);
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
    @Transactional
    public Honey addHoney(CreateHoneyCommand command) {
        Honey honey = toHoney(command);
        return repository.save(honey);
    }

    private Honey toHoney(CreateHoneyCommand command) {
        Honey honey = new Honey(command.getName(), command.getPrice(), command.getAmount(), command.getAvailable());
        Set<Manufacturer> manufacturerSet = fetchManufacturersByIds(command.getManufacturers());
        updateHoney(honey, manufacturerSet);
        return honey;
    }

    private void updateHoney(Honey honey, Set<Manufacturer> manufacturerSet) {
        honey.removeManufacturers();
        manufacturerSet.forEach(honey::addManufacturer);
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
            updateHoney(honey, fetchManufacturersByIds(command.getManufacturers()));
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
        log.info("Received honey: " + command.getFilename()
                + " bytes: " + length);
        repository.findById(command.getId())
                .ifPresent(honey -> {
                    Upload savedUpload = upload.save(new SaveUploadCommand(command.getFilename(), command.getFile(), command.getContentType()));
                    honey.setCoverId(savedUpload.getId());
                    repository.save(honey);
                });
    }

    @Override
    @Transactional
    public UpdateHoneyResponse updateHoney(UpdateHoneyCommand command) {
        return repository
                .findById(command.getId())
                .map(honey -> {
                    Honey updatedHoney = updateFields(command, honey);
//                    repository.save(updatedHoney); Hibernate changes the entity because of @Transactional
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
