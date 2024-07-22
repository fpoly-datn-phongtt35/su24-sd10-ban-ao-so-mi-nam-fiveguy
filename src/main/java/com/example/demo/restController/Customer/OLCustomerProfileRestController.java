package com.example.demo.restController.Customer;


import com.example.demo.entity.Customer;
import com.example.demo.entity.Employee;
import com.example.demo.security.service.SCAccountService;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.security.service.SCEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
}
