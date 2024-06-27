package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.ColorRequest;
import com.example.demo.service.thuong.ColorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/color")
public class ColorController {
    @Autowired
    private ColorService colorService;

    @GetMapping
    public ResponseEntity<?> getColors(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam String sortField,
                                        @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(colorService.getColor(page, size, keyword, sortField, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getColorById(@PathVariable Long id) {
        return ResponseEntity.ok(colorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createColor(@Valid @RequestBody ColorRequest color) {
        return new ResponseEntity<>(colorService.create(color), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateColor(@PathVariable Long id, @Valid @RequestBody ColorRequest color) {
        return new ResponseEntity<>(colorService.update(color, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(colorService.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteColor(@PathVariable Long id) {
        return new ResponseEntity<>(colorService.delete(id), HttpStatus.OK);
    }
}
