package com.jaybee.honey;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class HoneyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HoneyApplication.class, args);
    }

    private final CatalogService catalogService;

    public HoneyApplication(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Override
    public void run(String... args) {

        List<Honey> honeyList = catalogService.findByName("Small");
        honeyList.forEach(System.out::println);
    }
}
