package com.example.demo.repository.sale;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository2 extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByProductSalesIsNullAndStatusEquals(int status);


    @Query("SELECT ps.product FROM ProductSale ps " +
            "WHERE ps.product.status = 1 " +
            "GROUP BY ps.product " +
            "HAVING COUNT(ps) = SUM(CASE WHEN ps.sale.endDate < :currentDate THEN 1 ELSE 0 END)")
    List<Product> findByProductSalesEndDateBefore(Date currentDate);


}