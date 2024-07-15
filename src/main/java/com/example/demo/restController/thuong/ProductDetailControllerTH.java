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

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productDetailService.findAllByProduct_Id(id));
    }
}
