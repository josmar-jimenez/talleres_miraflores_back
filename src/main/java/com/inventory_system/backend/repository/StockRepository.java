package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.Stock;
import com.inventory_system.backend.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    Page<Store> findById(int id, Pageable pageable);

    @Query("SELECT s FROM Stock s WHERE s.product.id=?1 AND s.store.id=?2")
    Optional<Stock> findByProductIdAndStoreId(int productId, int storeId);

    Page<Stock> findByStore(Store store, Pageable pageable);
}
