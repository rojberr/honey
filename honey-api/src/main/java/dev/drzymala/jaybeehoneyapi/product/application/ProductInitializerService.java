package dev.drzymala.jaybeehoneyapi.product.application;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import dev.drzymala.jaybeehoneyapi.commons.BaseEntity;
import dev.drzymala.jaybeehoneyapi.product.application.port.ProductInitUseCase;
import dev.drzymala.jaybeehoneyapi.product.application.port.ProductUseCase;
import dev.drzymala.jaybeehoneyapi.product.application.port.ProductUseCase.CreateProductCommand;
import dev.drzymala.jaybeehoneyapi.product.db.ManufacturerJpaRepository;
import dev.drzymala.jaybeehoneyapi.product.domain.Manufacturer;
import dev.drzymala.jaybeehoneyapi.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductInitializerService implements ProductInitUseCase {

    private final ProductUseCase repository;
    private final ManufacturerJpaRepository manufacturerRepository;

    @Override
    @Transactional
    public void initialize() {
        initData();
    }

    private void initData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("products.csv").getInputStream()))) {
            CsvToBean<CsvProduct> build = new CsvToBeanBuilder<CsvProduct>(reader)
                    .withType(CsvProduct.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            build.stream().forEach(this::initProduct);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initProduct(CsvProduct csvProduct) {
        // parse authors
        Set<Long> manufacturers = Arrays.stream(csvProduct.manufacturer.split(","))
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .map(this::getOrCreateManufacturer)
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        CreateProductCommand command = new CreateProductCommand(
                csvProduct.name
        );
        Product product = repository.addProduct(command);
    }

    private Manufacturer getOrCreateManufacturer(String manufacturerName) {
        return manufacturerRepository
                .findByFirstNameIgnoreCase(manufacturerName)
                .orElseGet(() -> manufacturerRepository.save(new Manufacturer(manufacturerName, manufacturerName)));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CsvProduct {
        @CsvBindByName
        private String name;
        @CsvBindByName
        private String manufacturer;
    }
}
