package com.example.demo.restController;

import com.example.demo.entity.ProductSale;
import com.example.demo.service.ProductSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product-sales")
public class ProductSaleController {

    @Autowired
    private ProductSaleService productSaleService;

    @PostMapping
    public ResponseEntity<ProductSale> createProductSale(@RequestBody ProductSale productSale) {
        ProductSale createdProductSale = productSaleService.saveProductSale(productSale);
        return ResponseEntity.ok(createdProductSale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductSale> updateProductSale(@PathVariable Long id, @RequestBody ProductSale productSale) {
        ProductSale updatedProductSale = productSaleService.updateProductSale(id, productSale);
        return ResponseEntity.ok(updatedProductSale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductSale(@PathVariable Long id) {
        productSaleService.deleteProductSale(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSale> getProductSaleById(@PathVariable Long id) {
        ProductSale productSale = productSaleService.getProductSaleById(id);
        return ResponseEntity.ok(productSale);
    }

    @GetMapping
    public ResponseEntity<List<ProductSale>> getAllProductSales() {
        List<ProductSale> productSales = productSaleService.getAllProductSales();
        return ResponseEntity.ok(productSales);
    }
}