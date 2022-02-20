package com.jaybee.honey.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = "honeys")
public class Manufacturer {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "manufacturers")
    @JsonIgnoreProperties("manufacturers")
    private Set<Honey> honeys;

    @CreatedDate
    private LocalDateTime createdAt;

    public Manufacturer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
