package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OLImageRepository2 extends JpaRepository<Image, Long> {

    @Query("SELECT i.path FROM Image i " +
            "JOIN i.product p " +
            "JOIN i.color c " +
            "WHERE i.product.id = :productId " +
            "AND i.color.id = :colorId " +
            "AND i.status = 1 " +
            "AND p.status = 1 " +
            "AND c.status = 1")
    List<String> findPathsByProductIdAndColorId(@Param("productId") Long productId,
                                                @Param("colorId") Long colorId);


    @Query("SELECT i.path FROM Image i " +
            "JOIN i.product p " +
            "WHERE i.product.id = :productId " +
            "AND i.status = 1 " +
            "AND p.status = 1 " +
            "ORDER BY i.createdAt ASC")
    String findFirstImagePathByProductId(@Param("productId") Long productId);


}