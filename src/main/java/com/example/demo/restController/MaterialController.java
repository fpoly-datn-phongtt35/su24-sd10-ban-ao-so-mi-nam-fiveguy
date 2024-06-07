package com.example.demo.restController;

import com.example.demo.model.request.MaterialRequest;
import com.example.demo.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    @GetMapping
    public ResponseEntity<?> getMaterials(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam(required = false) String name,
                                        @RequestParam String sortField,
                                        @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(materialService.getMaterials(page, size, name, sortField, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createMaterial(@Valid @RequestBody MaterialRequest material) {
        return new ResponseEntity<>(materialService.create(material), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaterial(@PathVariable Long id, @Valid @RequestBody MaterialRequest material) {
        return new ResponseEntity<>(materialService.update(material, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(materialService.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long id) {
        return new ResponseEntity<>(materialService.delete(id), HttpStatus.OK);
    }
}
