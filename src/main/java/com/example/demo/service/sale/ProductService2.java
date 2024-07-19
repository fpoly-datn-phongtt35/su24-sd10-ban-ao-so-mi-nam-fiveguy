package com.example.demo.service.sale;

import com.example.demo.entity.Product;
import com.example.demo.model.response.sale.ProductDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService2 {
    List<Product> getProductsWithoutSaleOrExpiredPromotion();
//    List<Product> getProductsBySaleId(Long id);

    Page<ProductDTO> filterProducts(Long categoryId, Long collarId, Long wristId, Long colorId, Long sizeId, Long materialId, String searchTerm, int page, int size);

//    List<Product> searchByNameOrCode(String searchTerm);

}