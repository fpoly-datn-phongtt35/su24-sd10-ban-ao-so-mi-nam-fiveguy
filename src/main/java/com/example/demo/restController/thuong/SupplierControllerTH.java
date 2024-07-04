package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.SupplierRequestTH;
import com.example.demo.model.request.thuong.SupplierUpdateRequestTH;
import com.example.demo.service.thuong.SupplierServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/supplier")
public class SupplierControllerTH {
    @Autowired
    private SupplierServiceTH supplierServiceTH;

    @GetMapping
    public ResponseEntity<?> getSuppliers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam String sortField,
                                      @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(supplierServiceTH.getSuppliers(page, size, keyword, sortField, sortDirection));
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllSuppliers() {
        return ResponseEntity.ok(supplierServiceTH.findAllByStatus(1));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierServiceTH.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@Valid @RequestBody SupplierRequestTH supplier) {
        return new ResponseEntity<>(supplierServiceTH.create(supplier), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@Valid @RequestBody SupplierUpdateRequestTH supplier, @PathVariable Long id) {
        return new ResponseEntity<>(supplierServiceTH.update(supplier, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(supplierServiceTH.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        return new ResponseEntity<>(supplierServiceTH.delete(id), HttpStatus.OK);
    }

}
