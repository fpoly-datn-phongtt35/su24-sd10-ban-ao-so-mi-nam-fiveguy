package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Color;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NImageRepository extends JpaRepository<Image,Long> {

    List<Image> findImagesByProductIdOrderByCreatedAtAsc(Long id);

    List<Image> findByProductAndColor(Product product, Color color);

    List<Image> findAllByProductIdAndColorIdAndStatus(Long productId, Long colorId, Integer status);
}