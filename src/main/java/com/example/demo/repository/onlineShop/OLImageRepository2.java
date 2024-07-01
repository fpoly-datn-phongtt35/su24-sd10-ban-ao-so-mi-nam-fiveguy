package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OLImageRepository2 extends JpaRepository<Image, Long> {

    @Query("SELECT i.path FROM Image i WHERE i.product.id = :productId AND i.color.id = :colorId")
    List<String> findPathsByProductIdAndColorId(Long productId, Long colorId);
}