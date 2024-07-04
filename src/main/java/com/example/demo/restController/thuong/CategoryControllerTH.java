package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.CategoryRequestTH;
import com.example.demo.service.thuong.CategoryServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/category")
public class CategoryControllerTH {
    @Autowired
    private CategoryServiceTH categoryServiceTH;

    @GetMapping
    public ResponseEntity<?> getCategories(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(required = false) String name,
                                           @RequestParam String sortField,
                                           @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(categoryServiceTH.getCategories(page, size, name, sortField, sortDirection));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryServiceTH.findAllByStatus(1));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryServiceTH.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestTH category) {
        return new ResponseEntity<>(categoryServiceTH.create(category), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestTH category) {
        return new ResponseEntity<>(categoryServiceTH.update(category, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(categoryServiceTH.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        return new ResponseEntity<>(categoryServiceTH.delete(id), HttpStatus.OK);
    }
}
