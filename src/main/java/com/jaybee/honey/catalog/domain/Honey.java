package com.jaybee.honey.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jaybee.honey.jpa.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = "manufacturers")
public class Honey extends BaseEntity {

    private String name;
    private BigDecimal price;
    private Integer amount;
    private Long coverId;
    private Long available;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable
    @JsonIgnoreProperties("honeys")
    private Set<Manufacturer> manufacturers = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Honey(String productName, BigDecimal price, Integer amount, Long available) {

        this.name = productName;
        this.price = price;
        this.amount = amount;
        this.available = available;
    }

    public void addManufacturer(Manufacturer manufacturer) {
        manufacturers.add(manufacturer);
        manufacturer.getHoneys().add(this);
    }

    public void removeManufacturer(Manufacturer manufacturer) {
        manufacturers.remove(manufacturer);
        manufacturer.getHoneys().remove(this);
    }

    public void removeManufacturers() {
        Honey self = this;
        manufacturers.forEach(manufacturer -> manufacturer.getHoneys().remove(self));
        manufacturers.clear();
    }
}
