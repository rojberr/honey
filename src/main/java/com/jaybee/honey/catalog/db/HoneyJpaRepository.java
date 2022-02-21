package com.jaybee.honey.catalog.db;

import com.jaybee.honey.catalog.domain.Honey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoneyJpaRepository extends JpaRepository<Honey, Long> {

    List<Honey> findByManufacturers_firstNameContainsIgnoreCaseOrManufacturers_lastNameContainsIgnoreCase(String firstName, String lastName);

    List<Honey> findByNameStartsWithIgnoreCase(String title);

    @Query(
            " SELECT b FROM Honey b JOIN b.manufacturers a "
                    + " WHERE "
                    + " lower(a.firstName) LIKE lower(concat('%', :name, '%')) "
                    + " OR lower(a.lastName) LIKE lower(concat('%', :name, '%'))"
    )
    List<Honey> findByManufacturer(@Param("name") String name);
}
