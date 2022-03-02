package com.jaybee.honey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableSwagger2
public class HoneyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HoneyApplication.class, args);
    }
}
