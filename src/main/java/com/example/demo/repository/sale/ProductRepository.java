package com.example.demo.repository.sale;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductSalesIsNull();
//    List<Product> findByProductSalesEndDateBefore(Date currentDate);

    @Query("SELECT DISTINCT ps.product FROM ProductSale ps WHERE ps.sale.endDate < :currentDate")
    List<Product> findByProductSalesEndDateBefore(Date currentDate);

//    @Query("SELECT ps.product FROM ProductSale ps WHERE ps.sale.id = :saleId")
//    List<Product> findProductsBySaleId(@Param("saleId") Long saleId);
}