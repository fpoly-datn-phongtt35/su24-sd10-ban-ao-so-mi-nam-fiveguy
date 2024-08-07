package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Category;
import com.example.demo.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OLCategoryRepository2 extends JpaRepository<Category, Long> {


    @Query("SELECT c FROM Category c WHERE c.status = 1")
    List<Category> findAllActiveCategories();

    @Query(value = "SELECT p.category FROM Product p WHERE p.id = :productId")
    Category findCategoryByProductId(@Param("productId") Long productId);


}