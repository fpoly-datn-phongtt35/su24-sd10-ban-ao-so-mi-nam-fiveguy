package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OLProductDetailRepository2 extends JpaRepository<ProductDetail, Long>, JpaSpecificationExecutor<ProductDetail> {

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.id = :productId AND pd.status = :status")
    List<ProductDetail> findByProductIdAndStatus(@Param("productId") Long productId, @Param("status") Integer status);

    @Query("SELECT pd FROM ProductDetail pd " +
            "JOIN pd.product p " +
            "WHERE pd.product.id = :productId " +
            "AND pd.size.id = :sizeId " +
            "AND pd.color.id = :colorId " +
            "AND pd.status = 1 " +
            "AND pd.quantity > 1 " +
            "AND p.status = 1")
    ProductDetail findByProductIdAndSizeIdAndColorId(@Param("productId") Long productId,
                                                     @Param("sizeId") Long sizeId,
                                                     @Param("colorId") Long colorId);


}