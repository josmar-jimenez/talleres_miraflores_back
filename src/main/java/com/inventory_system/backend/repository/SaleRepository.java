package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.Sale;
import com.inventory_system.backend.model.Store;
import com.inventory_system.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {

    Page<Sale> findById(int id, Pageable pageable);

    Page<Sale> findByStore(Store store, Pageable pageable);

    List<Sale>findByCreatedGreaterThan(OffsetDateTime created);

    List<Sale>findByStoreAndCreatedGreaterThan(Store store, OffsetDateTime created);

    List<Sale>findByUserAndCreatedGreaterThan(User user, OffsetDateTime created);

    @Query("SELECT s FROM Sale s WHERE " +
            "LOWER(s.user.name) LIKE %?1% OR " +
            "LOWER(s.store.name) LIKE %?2% ")
    Page<Sale> findByUserNameContainingIgnoreCaseOrStoreNameContainingIgnoreCase(String userName, String storeName, Pageable pageable);

    @Query("SELECT s FROM Sale s WHERE " +
            " LOWER(s.user.name) LIKE %?1% AND LOWER(s.store.name) LIKE %?2% ")
    Page<Sale> findByUserNameContainingIgnoreCaseAndStoreContainingIgnoreCase(String userName, String storeName, Pageable pageable);
}
