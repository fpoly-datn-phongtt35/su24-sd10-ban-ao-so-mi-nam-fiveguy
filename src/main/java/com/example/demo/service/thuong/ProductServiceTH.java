package com.example.demo.service.thuong;

import com.example.demo.entity.Product;
import com.example.demo.model.request.thuong.ProductRequestTH;
import org.springframework.data.domain.Page;

public interface ProductServiceTH {
    Product create(ProductRequestTH productRequestTH);
    Product update(ProductRequestTH productRequestTH, Long id);
    Page<Product> getProducts(int page, int size, String keyword, String sortField, String sortDirection);
    Product findById(Long id);
}
