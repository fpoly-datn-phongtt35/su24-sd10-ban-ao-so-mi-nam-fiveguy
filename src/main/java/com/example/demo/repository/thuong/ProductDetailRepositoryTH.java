package com.example.demo.repository.thuong;

import com.example.demo.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepositoryTH extends JpaRepository<ProductDetail, Long> {
    List<ProductDetail> findAllByProduct_Id(Long id);
}
