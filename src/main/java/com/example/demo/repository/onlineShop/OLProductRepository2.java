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

public interface OLProductRepository2 extends JpaRepository<Product, Long> {

//get Product hiển thị
@Query("SELECT p.id, p.name, ps.discountPrice, s.value, s.discountType, " +
        "MIN(i.path) AS imagePath " +  // Selecting the first image path per product
        "FROM Product p " +
        "LEFT JOIN ProductSale ps ON p.id = ps.product.id " +
        "LEFT JOIN Sale s ON ps.sale.id = s.id AND s.status = 1 " +
        "LEFT JOIN p.images i ON i.product.id = p.id AND i.status = 1 " +  // Adding condition for image.status = 1
        "WHERE p.status = 1 " +
        "AND (ps.id IS NULL OR (ps.id IS NOT NULL AND (s.status = 1 OR s.status IS NULL))) " +
        "GROUP BY p.id, p.name, ps.discountPrice, s.value, s.discountType " +  // Grouping by all selected columns
        "ORDER BY p.id")
List<Object[]> findProductsWithImages();





    //get Product cho chi tiết sản phẩm
    @Query("SELECT p.id, p.name, p.price, w.name AS wristName, m.name AS materialName, " +
            "c.name AS categoryName, co.name AS collarName, " +
            "CASE WHEN ps.id IS NOT NULL THEN ps.promotionalPrice ELSE 0 END AS finalPrice, " +
            "COALESCE(sa.discountType, 0) AS discountType, " +
            "COALESCE(sa.value, 0) AS value " +
            "FROM Product p " +
            "LEFT JOIN p.wrist w " +
            "LEFT JOIN p.material m " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.collar co " +
            "LEFT JOIN p.productSales ps " +
            "LEFT JOIN ps.sale sa ON sa.status = 1 " +
            "WHERE p.id = :productId " +
            "AND p.status = 1 " +
            "AND (ps.id IS NULL OR (ps.id IS NOT NULL AND (sa.status = 1 OR sa.status IS NULL)))")
    List<Object[]> getProductInfo(@Param("productId") Long productId);



    //    get  promotionalPrice  hiển thị cart
@Query("SELECT ps.promotionalPrice " +
        "FROM Product p " +
        "LEFT JOIN ProductSale ps ON p.id = ps.product.id " +
        "WHERE p.id = :productId " +
        "AND p.status = 1 " +
        "AND (ps.id IS NULL OR (ps.id IS NOT NULL AND (ps.sale.status = 1 OR ps.sale.status IS NULL)))")
Integer findPromotionalPriceByProductId(@Param("productId") Long productId);

    @Query("SELECT p.id, p.name, ps.discountPrice, s.value, s.discountType, " +
            "MIN(i.path) AS imagePath " +  // Selecting the first image path per product
            "FROM Product p " +
            "JOIN p.productDetails pd " +
            "LEFT JOIN pd.billDetails bd " +
            "LEFT JOIN bd.bill b " +
            "LEFT JOIN ProductSale ps ON p.id = ps.product.id " +
            "LEFT JOIN Sale s ON ps.sale.id = s.id AND s.status = 1 " +
            "LEFT JOIN p.images i ON i.product.id = p.id " +
            "WHERE p.status = 1 AND b.status = 4 " +
            "GROUP BY p.id, p.name, p.price, ps.discountPrice, s.value, s.discountType " +  // Grouping by all selected columns
            "ORDER BY COALESCE(SUM(bd.quantity), 0) DESC")
    List<Object[]> findAllProductsOrderedByTotalQuantitySold();

    @Query("SELECT p.id, p.name, ps.discountPrice, s.value, s.discountType, " +
            "MIN(i.path) AS imagePath, p.createdAt " +  // Including p.createdAt in the SELECT clause
            "FROM Product p " +
            "LEFT JOIN ProductSale ps ON p.id = ps.product.id " +
            "LEFT JOIN Sale s ON ps.sale.id = s.id AND s.status = 1 " +
            "LEFT JOIN p.images i ON i.product.id = p.id AND i.status = 1" +
            "WHERE p.status = 1 " +
            "GROUP BY p.id, p.name, p.price, ps.discountPrice, s.value, s.discountType, p.createdAt " +  // Including p.createdAt in the GROUP BY clause
            "ORDER BY p.createdAt DESC")
    List<Object[]> findProductsOrderedByCreatedAt();


    @Query("SELECT p.id, p.name, ps.discountPrice, s.value, s.discountType, " +
            "MIN(i.path) AS imagePath " +
            "FROM Product p " +
            "LEFT JOIN ProductSale ps ON p.id = ps.product.id " +
            "LEFT JOIN Sale s ON ps.sale.id = s.id AND s.status = 1 " +
            "LEFT JOIN p.images i ON i.product.id = p.id AND i.status = 1 " +
            "WHERE p.status = 1 " +
            "AND (ps.id IS NULL OR (s.status = 1 OR s.status IS NULL)) " +
            "AND (p.name LIKE %:search% " +
            "OR EXISTS (SELECT 1 FROM p.productDetails pd " +
            "JOIN pd.color c WHERE c.name LIKE %:search%) " +
            "OR EXISTS (SELECT 1 FROM p.productDetails pd " +
            "JOIN pd.size sz WHERE sz.name LIKE %:search%) " +
            "OR EXISTS (SELECT 1 FROM p.material m WHERE m.name LIKE %:search%) " + // Tìm kiếm theo tên chất liệu
            "OR EXISTS (SELECT 1 FROM p.category ca WHERE ca.name LIKE %:search%)) " +
            "GROUP BY p.id, p.name, ps.discountPrice, s.value, s.discountType " +
            "ORDER BY p.id")
    List<Object[]> search(@Param("search") String search);




}
