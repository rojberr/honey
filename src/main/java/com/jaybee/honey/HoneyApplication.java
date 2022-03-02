package com.jaybee.honey;

import com.jaybee.honey.order.application.OrderProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties(OrderProperties.class)
public class HoneyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HoneyApplication.class, args);
    }
}
