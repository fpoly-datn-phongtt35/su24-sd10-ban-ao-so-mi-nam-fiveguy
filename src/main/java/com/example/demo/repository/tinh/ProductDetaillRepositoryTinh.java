package com.example.demo.repository.tinh;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetaillRepositoryTinh extends JpaRepository<ProductDetail, Long>, JpaSpecificationExecutor<ProductDetail> {

    List<ProductDetail> findAllByProductIdIn(List<Long> productIds);
}
