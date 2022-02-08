package com.jaybee.honey;

import com.jaybee.honey.catalog.application.CatalogController;
import com.jaybee.honey.catalog.domain.Honey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class ApplicationStartup implements CommandLineRunner {

    private final CatalogController catalogController;
    private final String query;
    private final Long limit;

    public ApplicationStartup(
            CatalogController catalogController,
            @Value("${honey.catalog.query}") String title,
            @Value("${honey.catalog.limit}") Long limit
    ) {
        this.catalogController = catalogController;
        this.query = title;
        this.limit = limit;
    }

    @Override
    public void run(String... args) {

        List<Honey> honeyList = catalogController.findByName(query);
        honeyList.stream().limit(limit).forEach(System.out::println);
    }
}
