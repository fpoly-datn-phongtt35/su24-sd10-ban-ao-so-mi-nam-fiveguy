package com.example.demo.service.sale;

import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<Product> getProductsWithoutSaleOrExpiredPromotion();
//    List<Product> getProductsBySaleId(Long id);

    Page<Product> filterProducts(Long categoryId, Long collarId, Long wristId, Long colorId, Long sizeId, Long materialId, String searchTerm, int page, int size);

//    List<Product> searchByNameOrCode(String searchTerm);

}