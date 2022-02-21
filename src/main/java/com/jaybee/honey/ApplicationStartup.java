package com.jaybee.honey;

import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.application.port.CatalogUseCase.CreateHoneyCommand;
import com.jaybee.honey.catalog.db.ManufacturerJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.catalog.domain.Manufacturer;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import com.jaybee.honey.order.application.port.QueryOrderUseCase;
import com.jaybee.honey.order.domain.OrderItem;
import com.jaybee.honey.order.domain.Recipient;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@AllArgsConstructor
class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase manipulateOrderUseCase;
    private final QueryOrderUseCase queryOrder;
    private final ManufacturerJpaRepository manufacturerRepository;

    @Override
    public void run(String... args) {
        initData();
        placeOrder();
    }

    private void initData() {
        Manufacturer honeyFirma = new Manufacturer("Honey", "Firma");
        Manufacturer firmaHoney = new Manufacturer("Firma", "Honey");
        manufacturerRepository.save(honeyFirma);
        manufacturerRepository.save(firmaHoney);

        CreateHoneyCommand myJar = new CreateHoneyCommand("My jar",
                Set.of(honeyFirma.getId()),
                new BigDecimal(15550),
                99);
        CreateHoneyCommand hisJar = new CreateHoneyCommand("His jar",
                Set.of(firmaHoney.getId(), honeyFirma.getId()),
                new BigDecimal(12345),
                22);
//        catalog.addHoney(new CreateHoneyCommand("Biggest jar", ,new BigDecimal("100.00"), 100));
//        catalog.addHoney(new CreateHoneyCommand("Big jar", ,new BigDecimal("75.00"), 75));
//        catalog.addHoney(new CreateHoneyCommand("Medium jar", ,BigDecimal.valueOf(50), 50));
//        catalog.addHoney(new CreateHoneyCommand("Small jar", ,new BigDecimal(25), 25));
//        catalog.addHoney(new CreateHoneyCommand("Beauty honey creme", ,new BigDecimal(10), 10));
        catalog.addHoney(myJar);
        catalog.addHoney(hisJar);
    }

    private void placeOrder() {
        Honey big_jar = catalog.findOneByName("His jar")
                .orElseThrow(() -> new IllegalStateException("Can't find the honey product"));
        Honey small_jar = catalog.findOneByName("My jar")
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

        queryOrder.findAll()
                .forEach(order -> System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order));
    }
}
