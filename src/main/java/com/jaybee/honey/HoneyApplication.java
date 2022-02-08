package com.jaybee.honey;

import com.jaybee.honey.catalog.domain.CatalogRepository;
import com.jaybee.honey.catalog.infrastructure.HealthHoneyRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HoneyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HoneyApplication.class, args);
    }

    @Bean
    CatalogRepository catalogRepository() {
        return new HealthHoneyRepository();
    }
}
