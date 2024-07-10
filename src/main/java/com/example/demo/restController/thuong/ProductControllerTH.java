package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.ProductRequestTH;
import com.example.demo.service.thuong.ProductServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/product")
public class ProductControllerTH {

    @Autowired
    private ProductServiceTH productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequestTH productRequestTH) throws IOException {
        System.out.println(productRequestTH.getProductDetails());
        return new ResponseEntity<>(productService.create(productRequestTH), HttpStatus.CREATED);
    }
}
