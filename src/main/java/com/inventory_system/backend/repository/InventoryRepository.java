package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.Inventory;
import com.inventory_system.backend.model.Store;
import com.inventory_system.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Page<Store> findById(int id, Pageable pageable);

    Page<Inventory> findByStore(Store store, Pageable pageable);

    long countByHasMismatchAndCreatedGreaterThan(boolean hasMismatch, OffsetDateTime created);

    long countByHasMismatchAndStoreAndCreatedGreaterThan(boolean hasMismatch,Store store, OffsetDateTime created);

    long countByHasMismatchAndUserAndCreatedGreaterThan(boolean hasMismatch,User user, OffsetDateTime created);

    @Query("SELECT i FROM Inventory i WHERE " +
            "LOWER(i.store.name) LIKE %?1%")
    Page<Inventory> findByStoreNameContainingIgnoreCase(String storeName, Pageable pageable);
}