package com.example.demo.restController.sale;

import com.example.demo.entity.ProductSale;
import com.example.demo.security.service.SCAccountService;
import com.example.demo.service.sale.ProductSaleService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/sales/product-sales")
public class ProductSaleRestController2 {

    @Autowired
    private ProductSaleService2 productSaleService2;

    @Autowired
    private SCAccountService accountService;

    @PostMapping
    public ResponseEntity<ProductSale> createProductSale(@RequestBody ProductSale productSale, @RequestHeader("Authorization") String token) {
        Optional<String> fullName = accountService.getFullNameByToken(token);

        if (fullName.isPresent()) {
            if (productSale.getId() == null) {
                productSale.setCreatedBy(fullName.get());
            }
        }

        ProductSale createdProductSale = productSaleService2.saveProductSale(productSale);
        return ResponseEntity.ok(createdProductSale);
    }


//    @PutMapping("/{id}")
//    public ResponseEntity<ProductSale> updateProductSale(@PathVariable Long id, @RequestBody ProductSale productSale) {
//        ProductSale updatedProductSale = productSaleService.updateProductSale(id, productSale);
//        return ResponseEntity.ok(updatedProductSale);
//    }
    
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteProductSale(@PathVariable Long id) {
            productSaleService2.deleteProductSale(id);
            return ResponseEntity.noContent().build();
        }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSale> getProductSaleById(@PathVariable Long id) {
        ProductSale productSale = productSaleService2.getProductSaleById(id);
        return ResponseEntity.ok(productSale);
    }

    @GetMapping
    public ResponseEntity<List<ProductSale>> getAllProductSales() {
        List<ProductSale> productSales = productSaleService2.getAllProductSales();
        return ResponseEntity.ok(productSales);
    }


    @GetMapping("/getList/{saleId}")
    public ResponseEntity<List<ProductSale>> getProductSalesBySaleId(@PathVariable Long saleId) {
        List<ProductSale> productSales = productSaleService2.getProductSalesBySaleId(saleId);
        return ResponseEntity.ok(productSales);
    }

    @PostMapping("/addList")
    public ResponseEntity<List<ProductSale>> addProductSales(@RequestBody List<ProductSale> productSales) {
        List<ProductSale> savedProductSales = productSaleService2.addProductSales(productSales);
        return ResponseEntity.ok(savedProductSales);
    }

//    @PostMapping("/addAll/{id}")
//    public ResponseEntity<List<ProductSale>> addAllProductSales(@PathVariable Long id) {
//        List<ProductSale> savedProductSales = productSaleService.addAllProductSales(id);
//        return ResponseEntity.ok(savedProductSales);
//    }

    @PostMapping("/deleteList")
    public ResponseEntity<Void> deleteProductSales(@RequestBody List<Long> ids) {
        productSaleService2.deleteProductSales(ids);
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("/deleteAll")
//    public ResponseEntity<Void> deleteAllProductSales() {
//        productSaleService.deleteAllProductSales();
//        return ResponseEntity.ok().build();
//    }

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

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return productSaleService2.filterProductSales(saleId, productId, categoryId, collarId, wristId, colorId, sizeId, materialId, status, searchTerm, pageable);
    }


}