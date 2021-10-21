package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.StockMovement;
import com.inventory_system.backend.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Integer> {

    Page<Store> findById(int id, Pageable pageable);

    Page<StockMovement> findBySourceStoreOrDestinyStore(Store sourceStore, Store destinyStore, Pageable pageable);
}
