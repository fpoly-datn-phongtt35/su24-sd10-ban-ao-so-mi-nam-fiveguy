package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.MaterialRequestTH;
import com.example.demo.service.thuong.MaterialServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/material")
public class MaterialControllerTH {
    @Autowired
    private MaterialServiceTH materialServiceTH;

    @GetMapping
    public ResponseEntity<?> getMaterials(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam(required = false) String name,
                                        @RequestParam String sortField,
                                        @RequestParam String sortDirection,
                                          @RequestParam(required = false) Integer status
    ) {

        return ResponseEntity.ok(materialServiceTH.getMaterials(page, size, name, sortField, sortDirection, status));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMaterials() {
        return ResponseEntity.ok(materialServiceTH.lstMaterials(1));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialServiceTH.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createMaterial(@Valid @RequestBody MaterialRequestTH material) {
        return new ResponseEntity<>(materialServiceTH.create(material), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaterial(@PathVariable Long id, @Valid @RequestBody MaterialRequestTH material) {
        return new ResponseEntity<>(materialServiceTH.update(material, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(materialServiceTH.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long id) {
        return new ResponseEntity<>(materialServiceTH.delete(id), HttpStatus.OK);
    }
}
