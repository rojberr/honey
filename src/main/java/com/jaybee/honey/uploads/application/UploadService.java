package com.jaybee.honey.uploads.application;

import com.jaybee.honey.uploads.application.port.UploadUseCase;
import com.jaybee.honey.uploads.db.UploadJpaRepository;
import com.jaybee.honey.uploads.domain.Upload;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UploadService implements UploadUseCase {
    private final UploadJpaRepository repository;

    @Override
    public Upload save(SaveUploadCommand command) {
//        String newId = RandomStringUtils.randomAlphanumeric(8).toLowerCase();
        Upload upload = new Upload(
                command.getFilename(),
                command.getContentType(),
                command.getFile()
        );
        repository.save(upload);
        System.out.println("Upload saved: " + upload.getFilename() + " with id: " + upload.getId());
        return upload;
    }

    @Override
    public Optional<Upload> getById(Long id) {
        return Optional.of(repository.getById(id));
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}
