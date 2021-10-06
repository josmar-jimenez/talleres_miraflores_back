package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    @Query
    Optional<Provider> findByName(String name);

    Page<Provider> findById(int id, Pageable pageable);
}
