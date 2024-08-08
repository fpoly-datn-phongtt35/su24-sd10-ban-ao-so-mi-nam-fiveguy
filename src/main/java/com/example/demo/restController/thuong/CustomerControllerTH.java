package com.example.demo.restController.thuong;

import com.example.demo.entity.Customer;
import com.example.demo.model.response.thuong.CustomerResponseTH;
import com.example.demo.security.service.SCAccountService;
import com.example.demo.service.thuong.CustomerServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/customer-th")
public class CustomerControllerTH {
    @Autowired
    private CustomerServiceTH customerService;

    @Autowired
    private SCAccountService accountService;

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerResponseTH customer, @RequestHeader("Authorization") String token) {
        Optional<String> name = accountService.getFullNameByToken(token);
        return new ResponseEntity<>(customerService.create(customer, name.get()), HttpStatus.CREATED);
    }
}
