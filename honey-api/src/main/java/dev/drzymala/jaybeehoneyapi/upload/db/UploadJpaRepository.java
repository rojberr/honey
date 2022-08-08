package dev.drzymala.jaybeehoneyapi.upload.db;

import dev.drzymala.jaybeehoneyapi.upload.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {

    List<Upload> findByFilenameStartsWithIgnoreCase(String filename);
}
