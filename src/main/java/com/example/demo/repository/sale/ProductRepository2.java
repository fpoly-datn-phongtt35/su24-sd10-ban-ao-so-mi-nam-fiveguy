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

    List<Product> findByProductSalesIsNull();

    @Query("SELECT DISTINCT ps.product FROM ProductSale ps WHERE ps.sale.endDate < :currentDate")
    List<Product> findByProductSalesEndDateBefore(Date currentDate);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchByNameOrCode(String searchTerm);
}