package dev.drzymala.jaybeehoneyapi.product.application.port;

import dev.drzymala.jaybeehoneyapi.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface ProductUseCase {

    public List<Product> findAll();

    Optional<Product> findById(Long id);

    public List<Product> findByProductName(String productName);

    public Optional<Product> findOneByProductName(String productName);

    List<Product> findByManufacturerName(String manufacturerName);

    Product addProduct(CreateProductCommand command);

    UpdateProductResponse updateProduct(UpdateProductCommand command);

    void updateProductCover(UpdateProductCoverCommand command);

    public void removeById(Long id);

    @Value
    class CreateProductCommand {
        String name;
    }

    @Value
    class UpdateProductCoverCommand {

        Long id;
        byte[] file;
        String contentType;
        String filename;
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateProductCommand {

        Long id;
        String name;
    }

    @Value
    class UpdateProductResponse {

        public static UpdateProductResponse SUCCESS = new UpdateProductResponse(true, emptyList());
        boolean success;
        List<String> errors;
    }
}
