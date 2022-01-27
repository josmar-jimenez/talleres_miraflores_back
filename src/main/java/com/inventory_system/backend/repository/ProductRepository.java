package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByShortNameOrBarcode(String shortName, String barcode);

    Page<Product> findById(int id, Pageable pageable);

    @Query(value = "Select p from Product p where p.id in :ids and p.status.id=1")
    List<Product> findAllActiveById(@Param("ids") List<Integer> ids);
}
