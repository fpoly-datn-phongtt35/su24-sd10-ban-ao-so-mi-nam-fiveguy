package com.example.demo.service.thuong;

import com.example.demo.entity.Product;
import com.example.demo.model.request.thuong.ProductRequestTH;
import com.example.demo.model.response.thuong.ProductResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface ProductServiceTH {
    Product create(ProductRequestTH productRequestTH);
    Product update(ProductRequestTH productRequestTH, Long id);
    Product updateStatus(Long id);
    Product delete(Long id);
    Page<ProductResponse> getProducts(int page, int size, String keyword, String sortField, String sortDirection, BigDecimal minPrice, BigDecimal maxPrice,Integer status);
    Product findById(Long id);
}
