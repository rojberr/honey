package dev.drzymala.jaybeehoneyapi.order.domain;

import dev.drzymala.jaybeehoneyapi.commons.BaseEntity;
import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipient extends BaseEntity {

    String name;

    String phone;

    String street;

    String city;

    String zipCode;

    String email;
}
