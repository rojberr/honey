package dev.drzymala.jaybeehoneyapi.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.drzymala.jaybeehoneyapi.commons.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = "products")
public class Manufacturer extends BaseEntity {

    private String firstName;
    private String lastName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "manufacturers", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("manufacturers")
    private Set<Product> products = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    public Manufacturer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
