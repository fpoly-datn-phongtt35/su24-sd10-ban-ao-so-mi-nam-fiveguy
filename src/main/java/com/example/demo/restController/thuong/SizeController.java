package com.example.demo.restController.thuong;


import com.example.demo.model.request.thuong.SizeRequest;
import com.example.demo.service.thuong.SizeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/size")
public class SizeController {
    @Autowired
    private SizeService sizeService;

    @GetMapping
    public ResponseEntity<?> getSizes(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size,
                                          @RequestParam(required = false) String name,
                                          @RequestParam String sortField,
                                          @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(sizeService.getSizes(page, size, name, sortField, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSizeById(@PathVariable Long id) {
        return ResponseEntity.ok(sizeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createSize(@Valid @RequestBody SizeRequest size) {
        return new ResponseEntity<>(sizeService.create(size), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSize(@PathVariable Long id, @Valid @RequestBody SizeRequest size) {
        return new ResponseEntity<>(sizeService.update(size, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(sizeService.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSize(@PathVariable Long id) {
        return new ResponseEntity<>(sizeService.delete(id), HttpStatus.OK);
    }
}
