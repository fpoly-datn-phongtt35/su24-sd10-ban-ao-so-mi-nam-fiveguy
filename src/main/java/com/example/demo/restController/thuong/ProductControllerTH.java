package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.ProductRequestTH;
import com.example.demo.security.service.SCAccountService;
import com.example.demo.service.thuong.ProductServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/product")
public class ProductControllerTH {

    @Autowired
    private ProductServiceTH productService;

    @Autowired
    private SCAccountService accountService;

    @GetMapping
    public ResponseEntity<?> getProducts(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(required = false) String keyword,
                                       @RequestParam String sortField,
                                       @RequestParam String sortDirection,
                                         @RequestParam(required = false) BigDecimal minPrice,
                                         @RequestParam(required = false) BigDecimal maxPrice,
                                         @RequestParam(required = false) Integer status

    ) {
        return ResponseEntity.ok(productService.getProducts(page, size, keyword, sortField, sortDirection, minPrice, maxPrice,status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequestTH productRequestTH, @RequestHeader("Authorization") String token) {
        Optional<String> fullName = accountService.getFullNameByToken(token);
        return new ResponseEntity<>(productService.create(productRequestTH, fullName.get()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateColor(@PathVariable Long id, @Valid @RequestBody ProductRequestTH productRequest, @RequestHeader("Authorization") String token) {
        Optional<String> fullName = accountService.getFullNameByToken(token);
        return new ResponseEntity<>(productService.update(productRequest, id, fullName.get()), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(productService.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return new ResponseEntity<>(productService.delete(id), HttpStatus.OK);
    }
}
