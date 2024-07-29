package com.example.demo.repository.thuong;

import com.example.demo.model.response.thuong.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductResponseRepositoryTH extends JpaRepository<ProductResponse, Long> {
    @Query(value = "SELECT p FROM ProductResponse p WHERE (p.price BETWEEN :minPrice AND :maxPrice) AND (p.name LIKE %:keyword% OR p.code LIKE %:keyword%) AND (:status IS NULL OR p.status = :status)")
    Page<ProductResponse> findByNameOrCode(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, @Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);
    @Query(value = "SELECT p FROM ProductResponse p WHERE (p.price BETWEEN :minPrice AND :maxPrice) AND (:status IS NULL OR p.status = :status)")
    Page<ProductResponse> findAllAndStatus(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, @Param("status") Integer status,Pageable pageable);
}
