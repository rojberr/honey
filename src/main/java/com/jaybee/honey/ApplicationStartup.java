package com.jaybee.honey;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.application.port.CatalogUseCase.UpdateHoneyCommand;
import com.jaybee.honey.catalog.application.port.CatalogUseCase.UpdateHoneyResponse;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.order.application.port.PlaceOrderUseCase;
import com.jaybee.honey.order.application.port.PlaceOrderUseCase.PlaceOrderCommand;
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
    private final PlaceOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final String query;
    private final Long limit;

    public ApplicationStartup(

            CatalogUseCase catalog,
            PlaceOrderUseCase placeOrder,
            QueryOrderUseCase queryOrder,
            @Value("${honey.catalog.query}") String query,
            @Value("${honey.catalog.limit}") Long limit
    ) {
        this.catalog = catalog;
        this.placeOrder = placeOrder;
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

    private void placeOrder() {
        Honey big_jar = catalog.findOneByName("Big jar").orElseThrow(() -> new IllegalStateException("Can't find the product"));
        Honey small_jar = catalog.findOneByName("Small jar").orElseThrow(() -> new IllegalStateException("Can't find the product"));

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
                .item(new OrderItem(big_jar, 16))
                .item(new OrderItem(small_jar, 2))
                .build();

        PlaceOrderUseCase.PlaceOrderResponse response = placeOrder.placeOrder(command);
        System.out.println("Created order with id: " + response.getOrderId());
        // list all orders
        queryOrder.findAll()
                .forEach(order ->
                        System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order
                        ));
    }

    private void searchCatalog() {
        findByName();
        findAndUpdate();
        findByName();
    }

    private void initData() {

        catalog.addHoney(new CatalogUseCase.CreateHoneyCommand("Big jar", new BigDecimal("75.00"), 75));
        catalog.addHoney(new CatalogUseCase.CreateHoneyCommand("Medium jar", BigDecimal.valueOf(50), 50));
        catalog.addHoney(new CatalogUseCase.CreateHoneyCommand("Small jar", new BigDecimal(25), 25));
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
                            .productName("Small jar")
                            .build();
                    UpdateHoneyResponse response = catalog.updateHoney(command);
                    System.out.println("Updating book result: " + response.isSuccess());
                });
    }
}
