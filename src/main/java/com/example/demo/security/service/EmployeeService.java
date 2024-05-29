package com.example.demo.security.service;

import com.example.demo.entity.Employee;

import java.util.Optional;

public interface EmployeeService {

    Optional<Employee> findById(Long Id);

    Employee findByAccount_Id(Long accountId);

    Employee save(Employee employees);
}
