package com.example.demo.repository.thuong;

import com.example.demo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepositoryTH extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND (:status IS NULL OR c.status = :status)")
    Page<Category> findByNameContainingIgnoreCaseAndStatus(String name, Integer status, Pageable pageable);
    @Query("SELECT c FROM Category c WHERE :status IS NULL OR c.status = :status")
    Page<Category> findAllAndStatus(@Param("status") Integer status, Pageable pageable);
    List<Category> findAllByStatus(Integer status);
    Category findByName(String name);
}
