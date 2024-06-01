package com.example.demo.security.service.impl;

import com.example.demo.entity.Employee;
import com.example.demo.security.repository.EmployeeRepository;
import com.example.demo.security.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service

public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee findByAccount_Id(Long accountId) {
        return employeeRepository.findByAccount_Id(accountId);
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }
}