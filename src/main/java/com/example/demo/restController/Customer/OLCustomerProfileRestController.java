package com.example.demo.restController.Customer;


import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerType;
import com.example.demo.entity.Employee;
import com.example.demo.security.service.SCAccountService;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.Customer.AccountServiceH;
import com.example.demo.service.Customer.CustomerServiceH;
import com.example.demo.service.Customer.CustomerTypeServiceH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/ol")
public class OLCustomerProfileRestController {
    
    @Autowired
    private SCAccountService accountService;

    @Autowired
    private SCEmployeeService scEmployeeService;

    @Autowired
    private SCCustomerService scCustomerService;

    @Autowired
    CustomerServiceH customerService;

    @Autowired
    AccountServiceH accountServiceH;

    @GetMapping(value = "/getUser")
    public Object getUserByToken(@RequestHeader("Authorization") String token) {
        Optional<Employee> employee = scEmployeeService.getEmployeeByToken(token);
        if (employee.isPresent()) {
            return employee.get();
        }
        Optional<Customer> customer = scCustomerService.getCustomerByToken(token);
        if (customer.isPresent()) {
            return customer.get();
        }
        return "User not found";
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer customers) {
        customerService.update(id, customers);
        if (customers != null) {
            return ResponseEntity.ok(customers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-user-account/{id}")
    public ResponseEntity<Account> updateAccount(@RequestHeader("Authorization") String token,@PathVariable Long id, @RequestBody Account customers) {
        Optional<Customer> customer = scCustomerService.getCustomerByToken(token);
        accountServiceH.updateAccount( customers, id);
        if (customers != null) {
            return ResponseEntity.ok(customers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}
