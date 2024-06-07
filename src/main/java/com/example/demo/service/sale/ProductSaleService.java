package com.example.demo.service.sale;

import com.example.demo.entity.ProductSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductSaleService {
    ProductSale saveProductSale(ProductSale productSale);
    ProductSale updateProductSale(Long id, ProductSale productSale);
    void deleteProductSale(Long id);
    ProductSale getProductSaleById(Long id);
    List<ProductSale> getAllProductSales();
    List<ProductSale> getProductSalesBySaleId(Long saleId);


    List<ProductSale> addProductSales(List<ProductSale> productSales);
    void deleteProductSales(List<Long> ids);
    void deleteAllProductSales();

    Page<ProductSale> filterProductSales(Long saleId, Long productId, Long categoryId, Long collarId, Long wristId, Long colorId, Long sizeId, Long materialId, Integer status, String searchTerm, Pageable pageable);
}