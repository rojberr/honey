package com.jaybee.honey.uploads.db;

import com.jaybee.honey.uploads.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {

    List<Upload> findByFilenameStartsWithIgnoreCase(String filename);
}
