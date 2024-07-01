package com.example.demo.security.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Employee;
import com.example.demo.security.jwt.JwtTokenUtil;
import com.example.demo.security.repository.SCAccountRepository;
import com.example.demo.security.repository.SCEmployeeRepository;
import com.example.demo.security.service.SCEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service

public class SCSCEmployeeServiceImpl implements SCEmployeeService {

    @Autowired
    private SCEmployeeRepository SCEmployeeRepository;

    @Autowired
    private SCAccountRepository SCAccountRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Employee findByAccount_Id(Long accountId) {
        return SCEmployeeRepository.findByAccount_Id(accountId);
    }

    @Override
    public Employee save(Employee employee) {
        return SCEmployeeRepository.save(employee);
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
            Optional<Account> optionalAccount = SCAccountRepository.findByAccount(accountName);
            if (optionalAccount.isEmpty()) {
                return Optional.empty();
            }

            Account account = optionalAccount.get();

            // Check for Employee with status = 1
            Employee employee = SCEmployeeRepository.findByAccount_Id(account.getId());
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