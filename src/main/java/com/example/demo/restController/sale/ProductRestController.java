package com.example.demo.restController.sale;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductSale;
import com.example.demo.service.sale.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/sales/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<List<Product>> getProductsWithoutSaleOrExpiredPromotion() {
        List<Product> products = productService.getProductsWithoutSaleOrExpiredPromotion();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/filter")
    public Page<Product> filterProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long collarId,
            @RequestParam(required = false) Long wristId,
            @RequestParam(required = false) Long colorId,
            @RequestParam(required = false) Long sizeId,
            @RequestParam(required = false) Long materialId,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return productService.filterProducts(categoryId, collarId, wristId, colorId, sizeId, materialId, searchTerm, page, size);
    }

}