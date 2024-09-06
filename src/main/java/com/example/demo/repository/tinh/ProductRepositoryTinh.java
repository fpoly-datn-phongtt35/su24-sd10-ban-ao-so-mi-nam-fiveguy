package com.example.demo.repository.tinh;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryTinh extends JpaRepository<Product, Long> {
    @Query("SELECT p, prdt FROM Product p JOIN ProductDetail prdt ON p.id = prdt.product.id")
    List<Object[]> findAllProductAndDetails();

    @Query("SELECT DISTINCT p FROM Product p WHERE p.status=1")
    Page<Product> findDistinctProducts(Pageable pageable);

    @Query("SELECT p, pd FROM Product p JOIN p.productDetails pd " +
            "WHERE (:totalQuantity IS NULL OR pd.quantity <= :totalQuantity) " +
            "GROUP BY p.id, pd.id")
    Page<Object[]> findFilteredProductsWithDetails(@Param("totalQuantity") Integer totalQuantity, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.status = 1")
    List<Product> findProductsWithStatusOne();
}
