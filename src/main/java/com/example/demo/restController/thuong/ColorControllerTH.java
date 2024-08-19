package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.ColorRequestTH;
import com.example.demo.service.thuong.ColorServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/color")
public class ColorControllerTH {
    @Autowired
    private ColorServiceTH colorServiceTH;

    @GetMapping
    public ResponseEntity<?> getColors(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam String sortField,
                                        @RequestParam String sortDirection,
                                       @RequestParam(required = false) Integer status
    ) {

        return ResponseEntity.ok(colorServiceTH.getColor(page, size, keyword, sortField, sortDirection, status));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllSuppliers() {
        return ResponseEntity.ok(colorServiceTH.findAllByStatus(1));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getColorById(@PathVariable Long id) {
        return ResponseEntity.ok(colorServiceTH.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createColor(@Valid @RequestBody ColorRequestTH color) {
        return new ResponseEntity<>(colorServiceTH.create(color), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateColor(@PathVariable Long id, @Valid @RequestBody ColorRequestTH color) {
        return new ResponseEntity<>(colorServiceTH.update(color, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(colorServiceTH.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteColor(@PathVariable Long id) {
        return new ResponseEntity<>(colorServiceTH.delete(id), HttpStatus.OK);
    }
}
