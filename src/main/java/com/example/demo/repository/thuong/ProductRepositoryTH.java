package com.example.demo.repository.thuong;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryTH extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Product findByCode(String code);
}
