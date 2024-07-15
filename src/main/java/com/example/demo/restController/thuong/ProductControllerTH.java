package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.ProductRequestTH;
import com.example.demo.service.thuong.ProductServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/product")
public class ProductControllerTH {

    @Autowired
    private ProductServiceTH productService;

    @GetMapping
    public ResponseEntity<?> getProducts(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(required = false) String keyword,
                                       @RequestParam String sortField,
                                       @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(productService.getProducts(page, size, keyword, sortField, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequestTH productRequestTH) {
        return new ResponseEntity<>(productService.create(productRequestTH), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateColor(@PathVariable Long id, @Valid @RequestBody ProductRequestTH productRequest) {
        return new ResponseEntity<>(productService.update(productRequest, id), HttpStatus.OK);
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
