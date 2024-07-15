package com.example.demo.repository.thuong;

import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryTH extends JpaRepository<Product, Long> {
    @Query(value = "SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.code LIKE %:keyword%")
    Page<Product> findByNameOrCode(@Param("keyword") String keyword, Pageable pageable);
    Product findByName(String name);
    Product findByCode(String code);
}
