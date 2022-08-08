package dev.drzymala.jaybeehoneyapi.product.db;

import dev.drzymala.jaybeehoneyapi.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    List<Product> findAll();

    List<Product> findByProductName(String productName);

    Optional<Product> findDistinctFirstByProductNameStartsWithIgnoreCase(String productName);

    @Query(
            " SELECT b FROM Product b JOIN b.manufacturers a "
                    + " WHERE "
                    + " lower(a.firstName) LIKE lower(concat('%', :manufacturerName, '%')) "
                    + " OR lower(a.lastName) LIKE lower(concat('%', :manufacturerName, '%'))"
    )
    List<Product> findByManufacturerName(@Param("manufacturerName") String manufacturerName);
}
