package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagTypeRepository extends JpaRepository<TagType, Integer> {
}
