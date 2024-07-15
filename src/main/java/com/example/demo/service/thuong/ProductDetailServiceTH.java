package com.example.demo.service.thuong;

import com.example.demo.entity.ProductDetail;

import java.util.List;

public interface ProductDetailServiceTH {
    List<ProductDetail> findAllByProduct_Id(Long id);
}
