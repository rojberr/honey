package com.jaybee.honey.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jaybee.honey.jpa.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = "honeys")
public class Manufacturer extends BaseEntity {

    private String firstName;
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "manufacturers")
    @JsonIgnoreProperties("manufacturers")
    private Set<Honey> honeys = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    public Manufacturer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addHoney(Honey honey) {
        honeys.add(honey);
        honey.getManufacturers().add(this);
    }

    public void removeHoney(Honey honey) {
        honeys.remove(honey);
        honey.getManufacturers().remove(this);
    }
}
