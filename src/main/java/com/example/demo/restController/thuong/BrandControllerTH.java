package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.BrandRequestTH;
import com.example.demo.service.thuong.BrandServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/brand")
public class BrandControllerTH {
    @Autowired
    private BrandServiceTH brandServiceTH;

    @GetMapping
    public ResponseEntity<?> getBrands(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam String sortField,
                                           @RequestParam String sortDirection,
                                            @RequestParam(required = false) Integer status
    ) {

        return ResponseEntity.ok(brandServiceTH.getBrands(page, size, keyword, sortField, sortDirection, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandServiceTH.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@Valid @RequestBody BrandRequestTH brand) {
        return new ResponseEntity<>(brandServiceTH.create(brand), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Long id, @Valid @RequestBody BrandRequestTH brand) {
        return new ResponseEntity<>(brandServiceTH.update(brand, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(brandServiceTH.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Long id) {
        return new ResponseEntity<>(brandServiceTH.delete(id), HttpStatus.OK);
    }

}
