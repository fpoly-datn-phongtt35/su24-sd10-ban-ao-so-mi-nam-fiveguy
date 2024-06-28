package com.example.demo.restController.nguyen;

import com.example.demo.service.nguyen.NProductPropertySerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/productProperty")
public class NProductPropertyRescontroller {

    @Autowired
    NProductPropertySerivce productPropertySerivce;

    @GetMapping("/all")
    public ResponseEntity<?> getAllProductProperty(){
        return ResponseEntity.ok(productPropertySerivce.getAllProductFilterProperty());
    }
}
