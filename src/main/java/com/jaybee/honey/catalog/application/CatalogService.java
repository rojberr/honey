package com.jaybee.honey.catalog.application;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.domain.CatalogRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.uploads.application.port.UploadUseCase;
import com.jaybee.honey.uploads.application.port.UploadUseCase.SaveUploadCommand;
import com.jaybee.honey.uploads.domain.Upload;
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
                .filter(honey -> honey.getName().toLowerCase().contains(productName.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Honey> findOneByName(String productName) {
        return repository.findAll()
                .stream()
                .filter(honey -> honey.getName().contains(productName))
                .findFirst();
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
        Honey honey = command.toHoney();
        return repository.save(honey);
    }

    @Override
    public void removeById(Long id) {

        repository.removeById(id);
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
                    Honey updatedHoney = command.updateFields(honey);
                    repository.save(updatedHoney);
                    return UpdateHoneyResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateHoneyResponse(false, Collections.singletonList("Honey not found with id: " + command.getId())));
    }
}
