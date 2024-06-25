package com.example.demo.restController;

import com.example.demo.model.request.CollarRequest;
import com.example.demo.service.CollarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/collar")
public class CollarController {

    @Autowired
    private CollarService collarService;

    @GetMapping
    public ResponseEntity<?> getCollars(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(required = false) String name,
                                           @RequestParam String sortField,
                                           @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(collarService.getCollars(page, size, name, sortField, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCollarById(@PathVariable Long id) {
        return ResponseEntity.ok(collarService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createCollar(@Valid @RequestBody CollarRequest collar) {
        return new ResponseEntity<>(collarService.create(collar), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCollar(@PathVariable Long id, @Valid @RequestBody CollarRequest collar) {
        return new ResponseEntity<>(collarService.update(collar, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(collarService.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCollar(@PathVariable Long id) {
        return new ResponseEntity<>(collarService.delete(id), HttpStatus.OK);
    }
}
