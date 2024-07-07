package com.example.demo.service.tinh;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDetailServiceTinh {
    List<ProductDetail> getAll();
    Page<ProductDetail> findProductDetal(String name, String code, BigDecimal price, Pageable pageable);
}
