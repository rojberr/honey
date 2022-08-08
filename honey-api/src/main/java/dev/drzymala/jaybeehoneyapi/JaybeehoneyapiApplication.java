package dev.drzymala.jaybeehoneyapi;

import dev.drzymala.jaybeehoneyapi.order.application.OrderProperties;
import dev.drzymala.jaybeehoneyapi.security.AdminConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({OrderProperties.class, AdminConfig.class})
public class JaybeehoneyapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaybeehoneyapiApplication.class, args);
    }

}
