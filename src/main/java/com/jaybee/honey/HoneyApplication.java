package com.jaybee.honey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HoneyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HoneyApplication.class, args);
    }

//    @Bean
//    CatalogRepository catalogRepository() {
//        return new HealthHoneyRepository();
//    }
}
