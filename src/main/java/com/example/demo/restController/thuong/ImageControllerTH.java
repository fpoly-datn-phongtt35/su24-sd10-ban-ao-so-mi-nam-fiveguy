package com.example.demo.restController.thuong;

import com.example.demo.model.response.thuong.FileResponse;
import com.example.demo.service.thuong.ImageServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.Resource;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

