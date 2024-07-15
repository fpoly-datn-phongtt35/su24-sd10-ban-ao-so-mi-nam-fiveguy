package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.CollarRequestTH;
import com.example.demo.service.thuong.CollarServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/collar")
public class CollarControllerTH {

    @Autowired
    private CollarServiceTH collarServiceTH;

    @GetMapping
    public ResponseEntity<?> getCollars(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(required = false) String name,
                                           @RequestParam String sortField,
                                           @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(collarServiceTH.getCollars(page, size, name, sortField, sortDirection));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCollars() {
        return ResponseEntity.ok(collarServiceTH.findAllByStatus(1));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCollarById(@PathVariable Long id) {
        return ResponseEntity.ok(collarServiceTH.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createCollar(@Valid @RequestBody CollarRequestTH collar) {
        return new ResponseEntity<>(collarServiceTH.create(collar), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCollar(@PathVariable Long id, @Valid @RequestBody CollarRequestTH collar) {
        return new ResponseEntity<>(collarServiceTH.update(collar, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(collarServiceTH.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCollar(@PathVariable Long id) {
        return new ResponseEntity<>(collarServiceTH.delete(id), HttpStatus.OK);
    }
}
