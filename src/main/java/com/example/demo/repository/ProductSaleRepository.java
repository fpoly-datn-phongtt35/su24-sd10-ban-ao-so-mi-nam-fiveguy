package com.example.demo.repository;


import com.example.demo.entity.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Long> {
    // You can add custom query methods here if needed
}