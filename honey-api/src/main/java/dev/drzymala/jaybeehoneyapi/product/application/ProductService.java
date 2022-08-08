package dev.drzymala.jaybeehoneyapi.product.application;

import dev.drzymala.jaybeehoneyapi.product.application.port.ProductUseCase;
import dev.drzymala.jaybeehoneyapi.product.db.ProductJpaRepository;
import dev.drzymala.jaybeehoneyapi.product.domain.Manufacturer;
import dev.drzymala.jaybeehoneyapi.product.domain.Product;
import dev.drzymala.jaybeehoneyapi.upload.application.port.UploadUseCase;
import dev.drzymala.jaybeehoneyapi.upload.domain.Upload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static dev.drzymala.jaybeehoneyapi.upload.application.port.UploadUseCase.SaveUploadCommand;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductJpaRepository repository;
    private final UploadUseCase upload;

    @Override
    public List<Product> findAll() {

        return repository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {

        return repository.findById(id);
    }

    @Override
    public List<Product> findByProductName(String productName) {

        return repository.findByProductName(productName);
    }

    @Override
    public Optional<Product> findOneByProductName(String productName) {

        return repository.findDistinctFirstByProductNameStartsWithIgnoreCase(productName);
    }


    @Override
    public List<Product> findByManufacturerName(String manufacturerName) {

        return repository.findByManufacturerName(manufacturerName);
    }

    private void updateProduct(Product product, Set<Manufacturer> manufacturerSet) {

        product.removeManufacturers();
        manufacturerSet.forEach(product::addManufacturer);
    }

    @Override
    @Transactional
    public Product addProduct(CreateProductCommand command) {

        Product honey = toProduct(command);
        return repository.save(honey);
    }

    @Override
    @Transactional
    public UpdateProductResponse updateProduct(UpdateProductCommand command) {

        return repository.findById(command.getId()).map(product -> {
            Product updatedHoney = updateFields(command, product);
//                    repository.save(updatedHoney); Hibernate changes the entity because of @Transactional
            return UpdateProductResponse.SUCCESS;
        }).orElseGet(() -> new UpdateProductResponse(false, Collections.singletonList("Honey not found with id: " + command.getId())));
    }

    private Product updateFields(UpdateProductCommand command, Product honey) {

        if (command.getName() != null) {
            honey.setProductName(command.getName());
        }
        return honey;
    }

    @Override
    public void updateProductCover(UpdateProductCoverCommand command) {

        int length = command.getFile().length;
        log.info("Received product: " + command.getFilename()
                + " bytes: " + length);
        repository.findById(command.getId())
                .ifPresent(honey -> {
                    Upload savedUpload = upload.save(new SaveUploadCommand(command.getFilename(), command.getFile(), command.getContentType()));
                    honey.setCoverId(savedUpload.getId());
                    repository.save(honey);
                });
    }

    @Override
    public void removeById(Long id) {

        repository.deleteById(id);
    }

    private Product toProduct(CreateProductCommand command) {

        Product honey = new Product(command.getName());
        return honey;
    }
}
