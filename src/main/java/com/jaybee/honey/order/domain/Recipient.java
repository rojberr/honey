package com.jaybee.honey.order.domain;

import com.jaybee.honey.jpa.BaseEntity;
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
