package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.SupplierRequest;
import com.example.demo.service.thuong.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public ResponseEntity<?> getSuppliers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      @RequestParam(required = false) String name,
                                      @RequestParam String sortField,
                                      @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(supplierService.getSuppliers(page, size, name, sortField, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@Valid @RequestBody SupplierRequest supplier) {
        return new ResponseEntity<>(supplierService.create(supplier), HttpStatus.CREATED);
    }
}
