package com.example.demo.restController;

//import com.example.demo.entity.AccountEntity
import com.example.demo.entity.Employee;
//import com.example.demo.model.request.employee.EmployeeRequest;
import com.example.demo.repository.EmployeeRepository;
//import com.example.demo.service.AccountService;
import com.example.demo.service.EmployeeService;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/employee")
public class EmployeeRestController {
    @Autowired
    EmployeeService employeeService;

//    @Autowired
//    AccountService accountService;

    @Autowired
    EmployeeRepository employeeRepository;


    @GetMapping("")
    public ResponseEntity<List<Employee>> getAll() {
        List<Employee> customers = employeeService.getAll();
        return ResponseEntity.ok(customers);

    }

    //get employee status =1
    @GetMapping("/status1")
    public ResponseEntity<List<Employee>> getAllStatusDangLam() {
        List<Employee> customers = employeeService.getAllStatusDangLam();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return ResponseEntity.ok(employee);
    }
    @GetMapping("/search-status/{id}")
    public ResponseEntity<List<Employee>> getAllstatus(@PathVariable Integer id) {
        List<Employee> employee =  employeeService.getAllStatus(id);
        return ResponseEntity.ok(employee);
    }


    @GetMapping("/get-page")
    public ResponseEntity<Page<Employee>> phantrang(@RequestParam(defaultValue = "0", name = "page") Integer t) {
        Page<Employee> employee = employeeService.phanTrang(t, 5);

        return ResponseEntity.ok(employee);
    }
     @PutMapping("/update-status-nhan-vien/{id}")
     public void updateStatus(@PathVariable Long id){
        employeeRepository.updateStatusEmployee(id);
     }


    //Thêm Employee

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Employee employees) {
        try {
            Employee createdEmployee = employeeService.create(employees);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // delete Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //update employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employees) {
        employeeService.update(id, employees);
        if (employees != null) {
            return ResponseEntity.ok(employees);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/status/{id}")
    public ResponseEntity<Employee> updatestatus(@PathVariable Long id, @RequestBody Employee employees) {
        employeeService.updateRole(id, employees);
        if (employees != null) {
            return ResponseEntity.ok(employees);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}