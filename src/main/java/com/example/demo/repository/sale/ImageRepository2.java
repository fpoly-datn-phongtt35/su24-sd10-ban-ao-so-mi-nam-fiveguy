package com.example.demo.repository.sale;

import com.example.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository2 extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.product.id = :productId AND i.status = 1 ORDER BY i.createdAt ASC")
    List<Image> findImagesByProductId(Long productId);


}