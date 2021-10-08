package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.Inventory;
import com.inventory_system.backend.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Page<Store> findById(int id, Pageable pageable);

    Page<Inventory> findByStore(Store store, Pageable pageable);
}
