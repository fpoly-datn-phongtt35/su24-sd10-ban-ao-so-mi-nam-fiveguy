package com.example.demo.restController.thuong;


import com.example.demo.service.thuong.BrandSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/brand-supplier")
public class BrandSupplierController {
    @Autowired
    private BrandSupplierService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findBySupplier_Id(id));
    }

}
