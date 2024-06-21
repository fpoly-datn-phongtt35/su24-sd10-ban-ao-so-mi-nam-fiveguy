package com.example.demo.security.RestControllerAccount;


import com.example.demo.entity.Employee;
import com.example.demo.security.jwt.JwtTokenUtil;
import com.example.demo.security.service.AccountService;
import com.example.demo.security.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin("*")
public class TestSecurityRestController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping(value = "/security/test", produces = "text/plain")
    public String hi(@RequestHeader("Authorization") String token) {
        Optional<String> fullName = accountService.getFullNameByToken(token);
        Optional<Employee> employee = employeeService.getEmployeeByToken(token);
        return "Hello, " + " Token " + token + " Name " + fullName.get() + employee.get();
    }
}