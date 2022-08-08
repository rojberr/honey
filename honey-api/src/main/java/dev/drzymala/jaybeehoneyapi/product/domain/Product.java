package dev.drzymala.jaybeehoneyapi.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.drzymala.jaybeehoneyapi.commons.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "manufacturers")
@EntityListeners(AuditingEntityListener.class)
public class Product extends BaseEntity {

    @Column(unique = true)
    private String productName;
    private BigDecimal price;
    private Integer amount;
    private Long coverId;
    private Long available;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable
    @JsonIgnoreProperties("products")
    private Set<Manufacturer> manufacturers = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Product(String productName) {

        this.productName = productName;
    }

    public void addManufacturer(Manufacturer manufacturer) {
        manufacturers.add(manufacturer);
        manufacturer.getProducts().add(this);
    }

    public void removeManufacturers() {
        Product self = this;
        manufacturers.forEach(manufacturer -> manufacturer.getProducts().remove(self));
        manufacturers.clear();
    }
}
