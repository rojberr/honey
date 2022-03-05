package com.jaybee.honey.catalog.application;

import com.jaybee.honey.catalog.application.port.CatalogInitializerUseCase;
import com.jaybee.honey.catalog.application.port.CatalogUseCase;
import com.jaybee.honey.catalog.db.ManufacturerJpaRepository;
import com.jaybee.honey.catalog.domain.Honey;
import com.jaybee.honey.catalog.domain.Manufacturer;
import com.jaybee.honey.jpa.BaseEntity;
import com.jaybee.honey.order.application.port.ManipulateOrderUseCase;
import com.jaybee.honey.order.application.port.QueryOrderUseCase;
import com.jaybee.honey.order.domain.Recipient;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jaybee.honey.catalog.application.port.CatalogUseCase.CreateHoneyCommand;
import static com.jaybee.honey.catalog.application.port.CatalogUseCase.UpdateHoneyCoverCommand;

@Slf4j
@Service
@AllArgsConstructor
public class CatalogInitializerService implements CatalogInitializerUseCase {

    private final CatalogUseCase catalog;
    private final QueryOrderUseCase queryOrder;
    private final ManipulateOrderUseCase manipulateOrderUseCase;
    private final ManufacturerJpaRepository manufacturerRepository;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public void initialize() {
        initData();
//        placeOrder();
    }

    private void initData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("honeys.csv").getInputStream()))) {
            CsvToBean<CsvHoney> build = new CsvToBeanBuilder<CsvHoney>(reader)
                    .withType(CsvHoney.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            build.stream().forEach(this::initHoney);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initHoney(CsvHoney csvHoney) {
        // parse authors
        Set<Long> manufacturers = Arrays.stream(csvHoney.manufacturer.split(","))
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .map(this::getOrCreateManufacturer)
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        CreateHoneyCommand command = new CreateHoneyCommand(
                csvHoney.name,
                manufacturers,
                csvHoney.price,
                csvHoney.amount,
                csvHoney.available
        );
        Honey honey = catalog.addHoney(command);
        catalog.updateHoneyCover(updateHoneyCoverCommand(honey.getId(), csvHoney.thumbnail));
    }

    private UpdateHoneyCoverCommand updateHoneyCoverCommand(Long id, String thumbnailUrl) {
        ResponseEntity<byte[]> response = restTemplate.exchange(thumbnailUrl,
                HttpMethod.GET,
                null,
                byte[].class);
        String contentType = response.getHeaders().getContentType().toString();
        return new UpdateHoneyCoverCommand(id, response.getBody(), contentType, "cover");
    }

    private Manufacturer getOrCreateManufacturer(String manufacturerName) {
        return manufacturerRepository
                .findByFirstNameIgnoreCase(manufacturerName)
                .orElseGet(() -> manufacturerRepository.save(new Manufacturer(manufacturerName, manufacturerName)));
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
        ManipulateOrderUseCase.PlaceOrderCommand command = ManipulateOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new ManipulateOrderUseCase.OrderItemCommand(big_jar.getId(), 5))
                .item(new ManipulateOrderUseCase.OrderItemCommand(small_jar.getId(), 10))
                .build();

        ManipulateOrderUseCase.PlaceOrderResponse response = manipulateOrderUseCase.placeOrder(command);
        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        log.info(result);

        queryOrder.findAll()
                .forEach(order -> log.info("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CsvHoney {
        @CsvBindByName
        private String name;
        @CsvBindByName
        private String manufacturer;
        @CsvBindByName
        private BigDecimal price;
        @CsvBindByName
        private Integer amount;
        @CsvBindByName
        private Long available;
        @CsvBindByName
        private String thumbnail;
    }

}
