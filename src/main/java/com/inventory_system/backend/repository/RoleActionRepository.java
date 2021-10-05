package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.RoleAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleActionRepository extends JpaRepository<RoleAction, Integer> {

    @Query("SELECT ra from RoleAction ra WHERE ra.role.id=:id")
    List<RoleAction> findByRoleId(int id);
}
