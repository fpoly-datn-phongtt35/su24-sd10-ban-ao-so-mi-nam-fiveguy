package com.example.demo.restController.thuong;

import com.example.demo.model.request.thuong.WristRequest;
import com.example.demo.service.thuong.WristService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/wrist")
public class WristController {
    @Autowired
    private WristService wristService;

    @GetMapping
    public ResponseEntity<?> getWrists(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam(required = false) String name,
                                        @RequestParam String sortField,
                                        @RequestParam String sortDirection
    ) {

        return ResponseEntity.ok(wristService.getWrists(page, size, name, sortField, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWristById(@PathVariable Long id) {
        return ResponseEntity.ok(wristService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createWrist(@Valid @RequestBody WristRequest wrist) {
        return new ResponseEntity<>(wristService.create(wrist), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWrist(@PathVariable Long id, @Valid @RequestBody WristRequest wrist) {
        return new ResponseEntity<>(wristService.update(wrist, id), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id) {
        return new ResponseEntity<>(wristService.updateStatus(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWirst(@PathVariable Long id) {
        return new ResponseEntity<>(wristService.delete(id), HttpStatus.OK);
    }
}
