package com.jaybee.honey.jpa;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

import static javax.persistence.GenerationType.AUTO;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode
abstract public class BaseEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String uuid = UUID.randomUUID().toString();
}
