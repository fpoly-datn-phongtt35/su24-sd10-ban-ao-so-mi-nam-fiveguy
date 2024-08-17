package com.example.demo.restController.point;

import com.example.demo.entity.CustomerType;
import com.example.demo.service.point.CustomerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/customer-types")
public class CustomerTypeController {

    @Autowired
    private  CustomerTypeService service;


    @GetMapping
    public Page<CustomerType> getCustomerTypes(@RequestParam(value = "name", required = false) String name, Pageable pageable) {

        return service.getCustomerTypes(name, pageable);
    }

    @GetMapping("/{id}")
    public CustomerType getCustomerTypeById(@PathVariable Long id) {
        return service.getCustomerTypeById(id);
    }

    @PostMapping
    public CustomerType createCustomerType(@RequestBody CustomerType customerType) {
        return service.saveCustomerType(customerType);
    }

    @PutMapping
    public CustomerType updateCustomerType(@RequestBody CustomerType customerType) {
        return service.updateCustomerType(customerType);
    }

}