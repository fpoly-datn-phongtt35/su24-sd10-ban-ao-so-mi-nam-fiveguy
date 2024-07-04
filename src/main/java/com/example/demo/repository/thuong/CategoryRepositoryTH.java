package com.example.demo.repository.thuong;

import com.example.demo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepositoryTH extends JpaRepository<Category, Long> {
    Page<Category> findByNameContainingIgnoreCase(String name,Pageable pageable);
    List<Category> findAllByStatus(Integer status);
    Category findByName(String name);
}
