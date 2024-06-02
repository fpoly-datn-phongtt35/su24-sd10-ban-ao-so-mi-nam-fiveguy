package com.example.demo.service.sale;

import com.example.demo.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProductsWithoutSaleOrExpiredPromotion();
//    List<Product> getProductsBySaleId(Long id);

    List<Product> filterProducts(Long categoryId, Long collarId, Long wristId, Long colorId, Long sizeId, Long materialId);

    List<Product> searchByNameOrCode(String searchTerm);

}