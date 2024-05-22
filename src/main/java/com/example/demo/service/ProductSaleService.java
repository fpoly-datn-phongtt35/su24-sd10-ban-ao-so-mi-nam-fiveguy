package com.example.demo.service;

import com.example.demo.entity.ProductSale;
import java.util.List;

public interface ProductSaleService {
    ProductSale saveProductSale(ProductSale productSale);
    ProductSale updateProductSale(Long id, ProductSale productSale);
    void deleteProductSale(Long id);
    ProductSale getProductSaleById(Long id);
    List<ProductSale> getAllProductSales();
}