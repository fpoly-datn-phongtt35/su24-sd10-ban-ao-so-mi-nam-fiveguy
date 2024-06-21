package com.example.demo.security.service;

import com.example.demo.entity.Employee;

import java.util.Optional;

public interface EmployeeService {

    Employee findByAccount_Id(Long accountId);

    Employee save(Employee employees);

    Optional<Employee> getEmployeeByToken(String token);
}
