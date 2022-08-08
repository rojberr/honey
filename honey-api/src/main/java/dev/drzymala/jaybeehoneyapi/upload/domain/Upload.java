package dev.drzymala.jaybeehoneyapi.upload.domain;

import dev.drzymala.jaybeehoneyapi.commons.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Upload extends BaseEntity {

    private byte[] file;

    private String contentType;

    private String filename;

    @CreatedDate
    private LocalDateTime createdAt;

    public Upload(String filename, String contentType, byte[] file) {
        this.file = file;
        this.contentType = contentType;
        this.filename = filename;
    }
}
