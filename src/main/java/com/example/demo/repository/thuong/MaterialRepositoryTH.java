package com.example.demo.repository.thuong;

import com.example.demo.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepositoryTH extends JpaRepository<Material, Long> {
    Page<Material> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Material> findAllByStatus(Integer status);
    Material findByName(String name);
}
