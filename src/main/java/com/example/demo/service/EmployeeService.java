package com.example.demo.service;

import com.example.demo.entity.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
     // get all
     List<Employee> getAll();
     // et all employee status
     List<Employee> getAllStatusDangLam();

     Employee getById(Long id);

     Page<Employee> phanTrang(Integer pageNum, Integer pageNo);

     Employee create(Employee employees);

     void delete(Long id);

     Employee update(Long id, Employee employees);

     Employee updateRole(Long id, Employee employees);

//     List<Employees> searchMa(@PathVariable String ma);
     Page<Employee>  searchMa(String ma, Integer page, Integer size);

     List<Employee> getAllStatus(Integer status);
}

