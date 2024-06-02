package com.example.demo.restController.sale;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductSale;
import com.example.demo.service.sale.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/sales/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping("/without-sale-or-expired-promotion")
    public ResponseEntity<List<Product>> getProductsWithoutSaleOrExpiredPromotion() {
        List<Product> products = productService.getProductsWithoutSaleOrExpiredPromotion();
        return ResponseEntity.ok(products);
    }

//    @GetMapping("/{saleId}")
//    public ResponseEntity<List<Product>> getProductSalesBySaleId(@PathVariable Long saleId) {
//        List<Product> productSales = productService.getProductsBySaleId(saleId);
//        return ResponseEntity.ok(productSales);
//    }

    @GetMapping("/filter")
    public List<Product> filterProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long collarId,
            @RequestParam(required = false) Long wristId,
            @RequestParam(required = false) Long colorId,
            @RequestParam(required = false) Long sizeId,
            @RequestParam(required = false) Long materialId) {

        return productService.filterProducts(categoryId, collarId, wristId, colorId, sizeId, materialId);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String searchTerm) {
        return productService.searchByNameOrCode(searchTerm);
    }
}