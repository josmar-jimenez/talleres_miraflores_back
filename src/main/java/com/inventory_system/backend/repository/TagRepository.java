package com.inventory_system.backend.repository;

import com.inventory_system.backend.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("SELECT t FROM Tag t WHERE t.name=?1 AND (?2 IS null OR t.type.id=?2)")
    Optional<Tag> findByNameAndTypeId(String name, Integer typeId);

    @Query("SELECT t FROM Tag t WHERE " +
            "LOWER(t.name) LIKE %?1% OR " +
            "LOWER(t.type.name) LIKE %?2% ")
    Page<Tag> findByTagNameContainingIgnoreCaseOrTypeNameContainingIgnoreCase(String name, String typeName, Pageable pageable);

    @Query("SELECT t FROM Tag t WHERE " +
            "LOWER(t.name) LIKE %?1% AND " +
            "LOWER(t.type.name) LIKE %?2% ")
    Page<Tag> findByTagNameContainingIgnoreCaseAndTypeNameContainingIgnoreCase(String name, String typeName, Pageable pageable);

}
