package com.example.demo.restController.thuong;

import com.example.demo.service.thuong.ProductDetailServiceTH;
import com.example.demo.service.thuong.ProductServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/product-detail")
public class ProductDetailControllerTH {

    @Autowired
    private ProductDetailServiceTH productDetailService;

    @GetMapping
    public ResponseEntity<?> getAll( @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(productDetailService.getAll(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productDetailService.findAllByProduct_Id(id));
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<?> getOneProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productDetailService.findById(id));
    }
}
