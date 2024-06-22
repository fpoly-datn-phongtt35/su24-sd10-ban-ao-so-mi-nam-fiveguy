package com.example.demo.restController;

import com.example.demo.model.request.BrandRequest;
import com.example.demo.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping
    public ResponseEntity<?> getBrands(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(required = false) String name,
                                           @RequestParam String sortField,
                                           @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(brandService.getBrands(page, size, name, sortField, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@Valid @RequestBody BrandRequest brand) {
        return new ResponseEntity<>(brandService.create(brand), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Long id, @Valid @RequestBody BrandRequest brand) {
        return new ResponseEntity<>(brandService.update(brand, id), HttpStatus.OK);
    }

}
