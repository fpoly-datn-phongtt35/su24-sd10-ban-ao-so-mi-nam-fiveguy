package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.WristRequestTH;
import com.example.demo.service.thuong.WristServiceTH;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/wrist")
public class WristControllerTH {
    @Autowired
    private WristServiceTH wristServiceTH;

    @GetMapping
    public ResponseEntity<?> getWrists(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam(required = false) String name,
                                        @RequestParam String sortField,
                                        @RequestParam String sortDirection,
                                       @RequestParam(required = false) Integer status
    ) {

        return ResponseEntity.ok(wristServiceTH.getWrists(page, size, name, sortField, sortDirection, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWristById(@PathVariable Long id) {
        return ResponseEntity.ok(wristServiceTH.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMaterials() {
        return ResponseEntity.ok(wristServiceTH.findAllByStatus(1));
    }

    @PostMapping
    public ResponseEntity<?> createWrist(@Valid @RequestBody WristRequestTH wrist) {
        return new ResponseEntity<>(wristServiceTH.create(wrist), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWrist(@PathVariable Long id, @Valid @RequestBody WristRequestTH wrist) {
        return new ResponseEntity<>(wristServiceTH.update(wrist, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(wristServiceTH.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWirst(@PathVariable Long id) {
        return new ResponseEntity<>(wristServiceTH.delete(id), HttpStatus.OK);
    }
}
