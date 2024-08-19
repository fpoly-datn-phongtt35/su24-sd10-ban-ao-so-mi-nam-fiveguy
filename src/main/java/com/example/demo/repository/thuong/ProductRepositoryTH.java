package com.example.demo.repository.thuong;

import com.example.demo.entity.Product;
import com.example.demo.model.response.thuong.ProductResponseTH;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepositoryTH extends JpaRepository<Product, Long> {
    @Query(value = "SELECT ps.promotionalPrice " +
            "FROM Product p " +
            "LEFT JOIN ProductSale ps ON p.id = ps.product.id " +
            "WHERE p.id = :productId " +
            "AND p.status = 1 " +
            "AND (ps.id IS NULL OR (ps.id IS NOT NULL AND (ps.sale.status = 1 OR ps.sale.status IS NULL)))")
    Integer findPromotionalPriceByProductId(@Param("productId") Long productId);

    @Query(value = "SELECT NEW com.example.demo.model.response.thuong.ProductResponseTH(p.id, p.code, p.name, p.price, p.createdAt, p.updatedAt, p.createdBy, p.updatedBy, p.status) FROM Product p WHERE (p.price BETWEEN :minPrice AND :maxPrice) AND (p.name LIKE %:keyword% OR p.code LIKE %:keyword%) AND (:status IS NULL OR p.status = :status)")
    Page<ProductResponseTH> findByNameOrCode(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, @Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);
    @Query(value = "SELECT NEW com.example.demo.model.response.thuong.ProductResponseTH(p.id, p.code, p.name, p.price, p.createdAt, p.updatedAt, p.createdBy, p.updatedBy, p.status) FROM Product p WHERE (p.price BETWEEN :minPrice AND :maxPrice) AND (:status IS NULL OR p.status = :status)")
    Page<ProductResponseTH> findAllAndStatus(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, @Param("status") Integer status, Pageable pageable);
    Product findByName(String name);
    Product findByCode(String code);
}
