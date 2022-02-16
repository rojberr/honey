package com.jaybee.honey.uploads.application;

import com.jaybee.honey.uploads.domain.Upload;
import lombok.Value;

public interface UploadUseCase {

    Upload save(SaveUploadCommand command);

    @Value
    class SaveUploadCommand {
        String filename;
        byte[] file;
        String contentType;
    }
}
