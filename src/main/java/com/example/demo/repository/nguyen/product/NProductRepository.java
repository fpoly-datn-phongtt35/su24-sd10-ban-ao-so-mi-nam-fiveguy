package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface NProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT MAX(p.price) FROM Product p")
    BigDecimal findMaxPrice();

    @Query("SELECT MIN(p.price) FROM Product p")
    BigDecimal   findMinPrice();
}
