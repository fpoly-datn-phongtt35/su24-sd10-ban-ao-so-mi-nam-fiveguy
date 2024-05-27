package com.example.demo.restController.sale;

import com.example.demo.entity.ProductSale;
import com.example.demo.service.sale.ProductSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/sales/product-sales")
public class ProductSaleRestController {

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


    @GetMapping("/getList/{saleId}")
    public ResponseEntity<List<ProductSale>> getProductSalesBySaleId(@PathVariable Long saleId) {
        List<ProductSale> productSales = productSaleService.getProductSalesBySaleId(saleId);
        return ResponseEntity.ok(productSales);
    }

    @PostMapping("/addList")
    public ResponseEntity<List<ProductSale>> addProductSales(@RequestBody List<ProductSale> productSales) {
        List<ProductSale> savedProductSales = productSaleService.addProductSales(productSales);
        return ResponseEntity.ok(savedProductSales);
    }


    @PostMapping("/deleteList")
    public ResponseEntity<Void> deleteProductSales(@RequestBody List<Long> ids) {
        productSaleService.deleteProductSales(ids);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllProductSales() {
        productSaleService.deleteAllProductSales();
        return ResponseEntity.ok().build();
    }

}