package dev.drzymala.jaybeehoneyapi.upload.application;

import dev.drzymala.jaybeehoneyapi.upload.application.port.UploadUseCase;
import dev.drzymala.jaybeehoneyapi.upload.db.UploadJpaRepository;
import dev.drzymala.jaybeehoneyapi.upload.domain.Upload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
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
        log.info("Upload saved: " + upload.getFilename() + " with id: " + upload.getId());
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
