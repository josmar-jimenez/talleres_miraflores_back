package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.Sale;
import com.inventory_system.backend.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {

    Page<Sale> findById(int id, Pageable pageable);

    Page<Sale> findByStore(Store store, Pageable pageable);
}
