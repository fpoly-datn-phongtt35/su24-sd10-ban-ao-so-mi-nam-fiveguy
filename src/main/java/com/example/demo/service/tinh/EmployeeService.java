package com.example.demo.service.tinh;

import com.example.demo.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface EmployeeService {
     // get all
     List<Employee> getAll();
     // et all employee status
     List<Employee> getAllStatusDangLam();

     Employee getById(Long id);

     Employee create(Employee employees);

     void delete(Long id);

     Employee update(Long id, Employee employees);

     Employee updateRole(Long id, Employee employees);

     //     List<Employees> searchMa(@PathVariable String ma);
     Page<Employee>  searchMa(String ma, Integer page, Integer size);

     List<Employee> getAllStatus(Integer status);

     Page<Employee> findEmployee(String fullName, String code, String avatar, Date birthDate, Boolean gender, String address, String account, Integer status, Pageable pageable);


}

