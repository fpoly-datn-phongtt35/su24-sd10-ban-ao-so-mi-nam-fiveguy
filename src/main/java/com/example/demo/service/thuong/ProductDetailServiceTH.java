package com.example.demo.service.thuong;

import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.thuong.ProductDetailResponseTH;

import java.util.List;

public interface ProductDetailServiceTH {
    List<ProductDetail> findAllByProduct_Id(Long id);
    List<ProductDetailResponseTH> getAll(String keyword);
    ProductDetail findById(Long id);
}
