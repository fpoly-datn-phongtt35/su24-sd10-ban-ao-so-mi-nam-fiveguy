package com.example.demo.restController.thuong;

import com.example.demo.entity.Customer;
import com.example.demo.service.thuong.CustomerServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/customer-th")
public class CustomerControllerTH {
    @Autowired
    private CustomerServiceTH customerService;

    @PostMapping
    public ResponseEntity<?> createColor(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.create(customer), HttpStatus.CREATED);
    }
}
