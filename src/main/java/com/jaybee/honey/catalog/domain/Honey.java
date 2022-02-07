package com.jaybee.honey.catalog.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class Honey {

    private final Long id;
    private final String productName;
    private final long price;
    private final Integer amount;
}
