package com.jaybee.honey.uploads.db;

import com.jaybee.honey.uploads.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {}
