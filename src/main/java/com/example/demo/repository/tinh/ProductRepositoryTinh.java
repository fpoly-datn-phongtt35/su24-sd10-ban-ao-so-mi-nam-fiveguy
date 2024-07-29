package com.example.demo.repository.tinh;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryTinh extends JpaRepository<Product, Long> {
    @Query("SELECT p, prdt FROM Product p JOIN ProductDetail prdt ON p.id = prdt.product.id")
    List<Object[]> findAllProductAndDetails();

    @Query("SELECT DISTINCT p FROM Product p")
    Page<Product> findDistinctProducts(Pageable pageable);
}
