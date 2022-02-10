package com.jaybee.honey;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.application.port.CatalogUseCase.UpdateHoneyCommand;
import com.jaybee.honey.catalog.application.port.CatalogUseCase.UpdateHoneyResponse;
import com.jaybee.honey.catalog.domain.Honey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final String query;
    private final Long limit;

    public ApplicationStartup(

            CatalogUseCase catalog,
            @Value("${honey.catalog.query}") String productName,
            @Value("${honey.catalog.limit}") Long limit
    ) {
        this.catalog = catalog;
        this.query = productName;
        this.limit = limit;
    }

    @Override
    public void run(String... args) {

        initData();
        findByName();
        findAndUpdate();
        findByName();
    }

    private void initData() {

        catalog.addHoney(new CatalogUseCase.CreateHoneyCommand("Big jar", BigDecimal.valueOf(75), 75));
        catalog.addHoney(new CatalogUseCase.CreateHoneyCommand("Medium jar", BigDecimal.valueOf(50), 50));
        catalog.addHoney(new CatalogUseCase.CreateHoneyCommand("Small jar", BigDecimal.valueOf(25), 25));
    }

    private void findByName() {
        List<Honey> honeyList = catalog.findByName(query);
        honeyList.stream().limit(limit).forEach(System.out::println);
    }

    private void findAndUpdate() {

        System.out.println("Updating Honey...");
        catalog.findOneByNameAndAmount("jar", 25)
                .ifPresent(honey -> {
                    UpdateHoneyCommand command = UpdateHoneyCommand.builder()
                            .id(honey.getId())
                            .productName("jar")
                            .build();
                    UpdateHoneyResponse response = catalog.updateHoney(command);
                    System.out.println("Updating book result: " + response.isSuccess());
                });
    }
}
