package com.jaybee.honey;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.application.port.CatalogUseCase.CreateHoneyCommand;
import com.jaybee.honey.catalog.application.port.CatalogUseCase.UpdateHoneyCommand;
import com.jaybee.honey.catalog.application.port.CatalogUseCase.UpdateHoneyResponse;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import com.jaybee.honey.order.application.port.QueryOrderUseCase;
import com.jaybee.honey.order.domain.OrderItem;
import com.jaybee.honey.order.domain.Recipient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase manipulateOrderUseCase;
    private final QueryOrderUseCase queryOrder;
    private final String query;
    private final Long limit;

    public ApplicationStartup(

            CatalogUseCase catalog,
            ManipulateOrderUseCase placeOrder,
            QueryOrderUseCase queryOrder,
            @Value("${honey.catalog.query}") String query,
            @Value("${honey.catalog.limit}") Long limit
    ) {
        this.catalog = catalog;
        this.manipulateOrderUseCase = placeOrder;
        this.queryOrder = queryOrder;
        this.query = query;
        this.limit = limit;
    }

    @Override
    public void run(String... args) {
        initData();
        searchCatalog();
        placeOrder();
    }

    private void initData() {
        catalog.addHoney(new CreateHoneyCommand("Biggest jar", new BigDecimal("100.00"), 100));
        catalog.addHoney(new CreateHoneyCommand("Big jar", new BigDecimal("75.00"), 75));
        catalog.addHoney(new CreateHoneyCommand("Medium jar", BigDecimal.valueOf(50), 50));
        catalog.addHoney(new CreateHoneyCommand("Small jar", new BigDecimal(25), 25));
    }

    private void searchCatalog() {
        findByName();
        findAndUpdate();
        findByName();
    }

    private void placeOrder() {
        Honey big_jar = catalog.findOneByName("Big jar")
                .orElseThrow(() -> new IllegalStateException("Can't find the honey product"));
        Honey small_jar = catalog.findOneByName("Small jar")
                .orElseThrow(() -> new IllegalStateException("Can't find the honey product"));

        // create recipient
        Recipient recipient = Recipient
                .builder()
                .name("Max Mustermann")
                .phone("+49 123456789")
                .street("Unter den Linden 1")
                .city("Berlin")
                .zipCode("12369")
                .email("max.mustermann@gmx.de")
                .build();

        // place order command
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(big_jar.getId(), 5))
                .item(new OrderItem(small_jar.getId(), 10))
                .build();

        PlaceOrderResponse response = manipulateOrderUseCase.placeOrder(command);
        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        System.out.println(result);
    }

    private void findByName() {
        List<Honey> honeyList = catalog.findByName(query);
        honeyList.forEach(System.out::println);
    }

    private void findAndUpdate() {
        System.out.println("Updating Honey...");
        catalog.findOneByNameAndAmount("jar", 25)
                .ifPresent(honey -> {
                    UpdateHoneyCommand command = UpdateHoneyCommand.builder()
                            .id(honey.getId())
                            .name("Small jar")
                            .build();
                    UpdateHoneyResponse response = catalog.updateHoney(command);
                    System.out.println("Updating honey result: " + response.isSuccess());
                });
    }
}
