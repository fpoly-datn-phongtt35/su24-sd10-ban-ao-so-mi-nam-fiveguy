package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Employee;
import com.example.demo.repository.Customer.OlEmployeeRepository;
import com.example.demo.service.Customer.OlEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OlEmployeeServiceImpl implements OlEmployeeService {

    @Autowired
    private OlEmployeeRepository employeeRepository;

    @Override
    public Optional<Employee> findById(Long Id) {
        Optional<Employee> employees = employeeRepository.findById(Id);

        if (employees.isPresent()){
            return employees;
        }

        return Optional.empty();
    }

    @Override
    public Employee findByAccount_Id(Long accountId) {
        return employeeRepository.findByAccount_Id(accountId);
    }

    @Override
    public Employee save(Employee employees) {
        return employeeRepository.save(employees);
    }
}
