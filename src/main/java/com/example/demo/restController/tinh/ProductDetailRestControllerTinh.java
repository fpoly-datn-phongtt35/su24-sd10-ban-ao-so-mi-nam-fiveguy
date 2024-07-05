package com.example.demo.restController.tinh;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import com.example.demo.service.tinh.ProductDetailServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/product-tinh")
public class ProductDetailRestControllerTinh {
    @Autowired
    ProductDetailServiceTinh productServiceTinh;

    @GetMapping("")
    public ResponseEntity<List<ProductDetail>> getAll(){
        List<ProductDetail> products= productServiceTinh.getAll();
        return ResponseEntity.ok(products);
    }
}
