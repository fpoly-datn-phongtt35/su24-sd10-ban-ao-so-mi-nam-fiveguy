package com.example.demo.repository.common;

import com.example.demo.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;




@Repository
public interface ProductDetailCommonRepository extends JpaRepository<ProductDetail, Long>, JpaSpecificationExecutor<ProductDetail> {




}

