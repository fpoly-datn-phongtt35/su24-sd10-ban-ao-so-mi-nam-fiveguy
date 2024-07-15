package com.example.demo.restController.thuong;

import com.example.demo.service.thuong.ImageServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/image")
public class ImageControllerTH {
    @Autowired
    private ImageServiceTH service;
    @GetMapping("/{id}")
    public ResponseEntity<?> getImagesById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findAllByProduct_Id(id));
    }
}
