package com.example.demo.repository.thuong;

import com.example.demo.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepositoryTH extends JpaRepository<ProductDetail, Long> {
}
