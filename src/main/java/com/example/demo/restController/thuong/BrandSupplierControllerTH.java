package com.example.demo.restController.thuong;


import com.example.demo.service.thuong.BrandSupplierServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/brand-supplier")
public class BrandSupplierControllerTH {
    @Autowired
    private BrandSupplierServiceTH service;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findBySupplier_Id(id));
    }

}
