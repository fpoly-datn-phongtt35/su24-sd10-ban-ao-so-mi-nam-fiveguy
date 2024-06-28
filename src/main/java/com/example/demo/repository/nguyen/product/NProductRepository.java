package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NProductRepository extends JpaRepository<Product,Long> {
}
