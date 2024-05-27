package com.example.demo.service.sale;

import com.example.demo.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProductsWithoutSaleOrExpiredPromotion();
//    List<Product> getProductsBySaleId(Long id);


}