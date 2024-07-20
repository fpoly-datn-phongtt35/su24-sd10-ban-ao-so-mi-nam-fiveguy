package com.example.demo.restController.sale;

import com.example.demo.entity.Product;
import com.example.demo.model.response.sale.ProductDTO;
import com.example.demo.service.sale.ProductService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/sales/products")
public class ProductRestController2 {

    @Autowired
    private ProductService2 productService2;

    @GetMapping("")
    public ResponseEntity<List<Product>> getProductsWithoutSaleOrExpiredPromotion() {
        List<Product> products = productService2.getProductsWithoutSaleOrExpiredPromotion();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/filter")
    public Page<ProductDTO> filterProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long collarId,
            @RequestParam(required = false) Long wristId,
            @RequestParam(required = false) Long colorId,
            @RequestParam(required = false) Long sizeId,
            @RequestParam(required = false) Long materialId,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return productService2.filterProducts(categoryId, collarId, wristId, colorId, sizeId, materialId, searchTerm, page, size);
    }

}