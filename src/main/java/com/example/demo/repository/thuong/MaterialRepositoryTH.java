package com.example.demo.repository.thuong;

import com.example.demo.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepositoryTH extends JpaRepository<Material, Long> {
    @Query("SELECT m FROM Material m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) AND (:status IS NULL OR m.status = :status)")
    Page<Material> findByNameContainingIgnoreCaseAndStatus(@Param("name") String name, @Param("status") Integer status, Pageable pageable);
    @Query("SELECT m FROM Material m WHERE :status IS NULL OR m.status = :status")
    Page<Material> findAllAndStatus(@Param("status") Integer status, Pageable pageable);
    List<Material> findAllByStatus(Integer status);
    Material findByName(String name);
}
