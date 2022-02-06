package com.jaybee.honey;

import com.jaybee.honey.catalog.application.CatalogController;
import com.jaybee.honey.catalog.domain.Honey;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationStartup implements CommandLineRunner {

    private final CatalogController catalogController;


    public ApplicationStartup(CatalogController catalogController) {
        this.catalogController = catalogController;
    }

    @Override
    public void run(String... args) {

        List<Honey> honeyList = catalogController.findByName("Small");
        honeyList.forEach(System.out::println);
    }
}
