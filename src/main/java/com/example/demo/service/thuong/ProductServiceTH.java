package com.example.demo.service.thuong;

import com.example.demo.entity.Product;
import com.example.demo.model.request.thuong.ProductRequestTH;
import com.example.demo.model.response.thuong.ProductResponseTH;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface ProductServiceTH {
    Product create(ProductRequestTH productRequestTH, String fullName);
    Product update(ProductRequestTH productRequestTH, Long id, String fullName);
    Product updateStatus(Long id);
    Product delete(Long id);
    Page<ProductResponseTH> getProducts(int page, int size, String keyword, String sortField, String sortDirection, BigDecimal minPrice, BigDecimal maxPrice, Integer status);
    Product findById(Long id);
}
