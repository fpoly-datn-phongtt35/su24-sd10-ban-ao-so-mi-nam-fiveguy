package com.example.demo.restController.sale;

import com.example.demo.entity.ProductSale;
import com.example.demo.service.sale.ProductSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/fillProductSale")
    public Page<ProductSale> filterProductSales(
            @RequestParam(required = false) Long saleId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long collarId,
            @RequestParam(required = false) Long wristId,
            @RequestParam(required = false) Long colorId,
            @RequestParam(required = false) Long sizeId,
            @RequestParam(required = false) Long materialId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm) {

        System.out.println("saleId: " + saleId);
        System.out.println("productId: " + productId);
        System.out.println("categoryId: " + categoryId);
        System.out.println("collarId: " + collarId);
        System.out.println("wristId: " + wristId);
        System.out.println("colorId: " + colorId);
        System.out.println("sizeId: " + sizeId);
        System.out.println("materialId: " + materialId);
        System.out.println("status: " + status);
        System.out.println("searchTerm: " + searchTerm);

        Pageable pageable = PageRequest.of(page, size);
        return productSaleService.filterProductSales(saleId, productId, categoryId, collarId, wristId, colorId, sizeId, materialId, status, searchTerm, pageable);
    }


}