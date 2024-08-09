package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface NProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT MAX(p.price) FROM Product p")
    BigDecimal findMaxPrice();

    @Query("SELECT MIN(p.price) FROM Product p")
    BigDecimal   findMinPrice();

    @Query("SELECT ps.promotionalPrice " +
            "FROM Product p " +
            "LEFT JOIN ProductSale ps ON p.id = ps.product.id " +
            "WHERE p.id = :productId " +
            "AND p.status = 1 " +
            "AND (ps.id IS NULL OR (ps.id IS NOT NULL AND (ps.sale.status = 1 OR ps.sale.status IS NULL)))")
    Integer findPromotionalPriceByProductId(@Param("productId") Long productId);
}
