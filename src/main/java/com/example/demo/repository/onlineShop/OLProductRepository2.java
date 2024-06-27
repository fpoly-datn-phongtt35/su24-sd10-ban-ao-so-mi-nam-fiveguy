package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OLProductRepository2 extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {


    @Query("SELECT p.id, p.name, p.price, ps.discountPrice, ps.promotionalPrice, s.value, s.discountType, " +
            "       MIN(i.path) AS imagePath " +  // Selecting the first image path per product
            "FROM Product p " +
            "LEFT JOIN ProductSale ps ON p.id = ps.product.id " +
            "LEFT JOIN Sale s ON ps.sale.id = s.id AND s.status = 1 " +
            "LEFT JOIN p.images i ON i.product.id = p.id " +
            "WHERE p.status = 1 " +
            "AND (ps.id IS NULL OR (ps.id IS NOT NULL AND (s.status = 1 OR s.status IS NULL))) " +
            "GROUP BY p.id, p.name, p.price, ps.discountPrice, ps.promotionalPrice, s.value, s.discountType " +  // Grouping by all selected columns
            "ORDER BY p.id")
    List<Object[]> findProductsWithImages();

}
