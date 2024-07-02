package com.example.demo.restController.thuong;


import com.example.demo.model.request.thuong.SizeRequestTH;
import com.example.demo.service.thuong.SizeServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/size")
public class SizeControllerTH {
    @Autowired
    private SizeServiceTH sizeServiceTH;

    @GetMapping
    public ResponseEntity<?> getSizes(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size,
                                          @RequestParam(required = false) String name,
                                          @RequestParam String sortField,
                                          @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(sizeServiceTH.getSizes(page, size, name, sortField, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSizeById(@PathVariable Long id) {
        return ResponseEntity.ok(sizeServiceTH.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createSize(@Valid @RequestBody SizeRequestTH size) {
        return new ResponseEntity<>(sizeServiceTH.create(size), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSize(@PathVariable Long id, @Valid @RequestBody SizeRequestTH size) {
        return new ResponseEntity<>(sizeServiceTH.update(size, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(sizeServiceTH.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSize(@PathVariable Long id) {
        return new ResponseEntity<>(sizeServiceTH.delete(id), HttpStatus.OK);
    }
}
