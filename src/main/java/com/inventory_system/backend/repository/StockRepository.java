package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.Stock;
import com.inventory_system.backend.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    Page<Stock> findById(int id, Pageable pageable);

    @Query("SELECT s FROM Stock s WHERE s.product.id=?1 AND s.store.id=?2")
    Optional<Stock> findByProductIdAndStoreId(int productId, int storeId);

    Page<Stock> findByStore(Store store, Pageable pageable);

    @Query("SELECT s FROM Stock s WHERE s.product.id=?1 AND s.product.status.id=1 and s.store.status.id=1 and s.status.id=1")
    List<Stock> findByProductId(int productId);

    List<Stock> findByStockLessThan(Long minimumStock);

    List<Stock> findByStoreAndStockLessThan(Store store, Long minimumStock);

    @Query("SELECT s FROM Stock s WHERE " +
            "LOWER(s.product.name) LIKE %?1% OR " +
            "LOWER(s.store.name) LIKE %?2% ")
    Page<Stock> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String productName, String storeName,Pageable pageable);

    @Query("SELECT s FROM Stock s WHERE " +
            " LOWER(s.product.name) LIKE %?1% AND LOWER(s.store.name) LIKE %?2% ")
    Page<Stock> findByNameContainingIgnoreCaseAndCodeContainingIgnoreCase(String productName, String storeName,Pageable pageable);
}
