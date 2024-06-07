package com.example.demo.repository;

import com.example.demo.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Page<Material> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Material findByName(String name);
}
