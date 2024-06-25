package com.example.demo.repository.sale;


import com.example.demo.entity.Product;
import com.example.demo.entity.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Long>, JpaSpecificationExecutor<ProductSale> {

    List<ProductSale> findBySaleId(Long saleId);


}