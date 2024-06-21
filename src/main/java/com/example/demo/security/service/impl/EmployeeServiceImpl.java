package com.example.demo.security.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Employee;
import com.example.demo.security.jwt.JwtTokenUtil;
import com.example.demo.security.repository.AccountRepository;
import com.example.demo.security.repository.EmployeeRepository;
import com.example.demo.security.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service

public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Employee findByAccount_Id(Long accountId) {
        return employeeRepository.findByAccount_Id(accountId);
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> getEmployeeByToken(String token) {
        try {
            // Validate token
            if (token == null || !token.startsWith("Bearer ")) {
                return Optional.empty();
            }

            // Extract actual token
            String actualToken = token.replace("Bearer ", "");

            // Get account name from token
            String accountName = jwtTokenUtil.getUsernameFromToken(actualToken);
            if (accountName == null) {
                return Optional.empty();
            }

            // Retrieve account
            Optional<Account> optionalAccount = accountRepository.findByAccount(accountName);
            if (optionalAccount.isEmpty()) {
                return Optional.empty();
            }

            Account account = optionalAccount.get();

            // Check for Employee with status = 1
            Employee employee = employeeRepository.findByAccount_Id(account.getId());
            if (employee != null) {
                return Optional.of(employee);
            }
        } catch (Exception e) {
            // Log exception (use a logging framework in real applications)
            System.err.println("Error occurred while getting employee by token: " + e.getMessage());
        }

        return Optional.empty();
    }

}