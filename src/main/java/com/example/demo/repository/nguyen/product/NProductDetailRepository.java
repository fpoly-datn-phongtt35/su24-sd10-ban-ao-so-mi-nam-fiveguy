package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface NProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    @Query("SELECT pd FROM ProductDetail pd " +
            "JOIN pd.product p " +
            "WHERE (:productName IS NULL OR p.name LIKE %:productName%) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:materialId IS NULL OR p.material.id = :materialId) " +
            "AND (:wristId IS NULL OR p.wrist.id = :wristId) " +
            "AND (:collarId IS NULL OR p.collar.id = :collarId) " +
            "AND (:sizeId IS NULL OR pd.size.id = :sizeId) " +
            "AND (:colorId IS NULL OR pd.color.id = :colorId) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)" +
            "AND (pd.status = :statusPd) ORDER BY pd.id desc")
    Page<ProductDetail> searchProductDetails(@Param("productName") String productName,
                                             @Param("categoryId") Long categoryId,
                                             @Param("materialId") Long materialId,
                                             @Param("wristId") Long wristId,
                                             @Param("collarId") Long collarId,
                                             @Param("sizeId") Long sizeId,
                                             @Param("colorId") Long colorId,
                                             @Param("minPrice") BigDecimal minPrice,
                                             @Param("maxPrice") BigDecimal maxPrice,
                                             @Param("statusPd") Integer statusPd,
                                             Pageable pageable);

}
