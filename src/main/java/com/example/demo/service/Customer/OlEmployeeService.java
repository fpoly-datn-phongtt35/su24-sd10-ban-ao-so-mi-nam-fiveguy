package com.example.demo.service.Customer;


import com.example.demo.entity.Employee;

import java.util.Optional;

public interface OlEmployeeService {

    Optional<Employee> findById(Long Id);

    Employee findByAccount_Id(Long accountId);

    Employee save(Employee employees);


}
