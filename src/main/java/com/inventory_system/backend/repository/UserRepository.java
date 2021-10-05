package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.Store;
import com.inventory_system.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query
    Optional<User> findByNick(String nick);

    Page<User> findByStore(Store store, Pageable pageable);

    Page<User> findById(int id, Pageable pageable);

}
