package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NProductSaleRepository extends JpaRepository<ProductSale,Long> {
}