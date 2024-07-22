package com.example.demo.restController.Customer;

import com.example.demo.entity.CustomerType;
import com.example.demo.service.Customer.CustomerTypeServiceH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/customerType")

public class CustomerTypeRestControllerH {

    @Autowired
    CustomerTypeServiceH customerTypeServiceH;

    @GetMapping("")
    public ResponseEntity<List<CustomerType>> getAll() {
        List<CustomerType> customerTypes = customerTypeServiceH.getAll();
        return ResponseEntity.ok(customerTypes);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomerType> getById(@PathVariable Long id) {
        CustomerType customerType = customerTypeServiceH.getById(id);
        return ResponseEntity.ok(customerType);
    }
    @GetMapping("/get-page")
    public ResponseEntity<Page<CustomerType>> phantrang(@RequestParam(defaultValue = "0", name = "page") Integer t) {
        Page<CustomerType> customerTypes = customerTypeServiceH.phanTrang(t, 5);

        return ResponseEntity.ok(customerTypes);
    }
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody CustomerType customerType) {
        try {
            CustomerType createdCustomerType = customerTypeServiceH.create(customerType);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomerType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerTypeServiceH.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<CustomerType> update(@PathVariable Long id, @RequestBody CustomerType customerType) {
        customerTypeServiceH.update(id, customerType);
        if (customerType != null) {
            return ResponseEntity.ok(customerType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
