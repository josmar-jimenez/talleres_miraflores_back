package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.SaleDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleDetailRepository extends JpaRepository<SaleDetail, Integer> {

    Page<SaleDetail> findById(int id, Pageable pageable);
}
