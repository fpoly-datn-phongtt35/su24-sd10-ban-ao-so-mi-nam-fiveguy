package com.example.demo.restController.tinh;

//import com.example.demo.entity.AccountEntity

import com.example.demo.entity.Employee;
import com.example.demo.repository.tinh.EmployeeRepository;
import com.example.demo.service.tinh.EmployeeService;
import com.example.demo.untility.tinh.PaginationResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/employee")
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



    @PutMapping("/update-status-nhan-vien/{id}")
    public void updateStatus(@PathVariable Long id){
        employeeRepository.updateStatusEmployee(id);
    }


    //Thêm Employee

    @PostMapping("/save")
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
    @GetMapping("/page")
    public PaginationResponse<Employee> getEmployees(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String avatar,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) Date birthDate,
            @RequestParam(required = false) Boolean gender,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Long idRole,
            @RequestParam(required = false) Integer status,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size,
//            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 5);
        Page<Employee> page = employeeService.findEmployee(fullName, code, avatar, birthDate, gender, address, account, email, phoneNumber, idRole, status, pageable);
        return new PaginationResponse<>(page);
    }

//    private static CellStyle cellStyleFormatNumber = null;
//    @GetMapping("/excel")
//    public void fileExcel() {
//        try {
//            XSSFWorkbook worbook = new XSSFWorkbook();
//            XSSFSheet sheet = worbook.createSheet("List of Employee");
//
//            XSSFRow row = null;
//            Cell cell = null;
//            LocalDateTime date = LocalDateTime.now();
//            DateTimeFormatter getDate = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
//            row = sheet.createRow(0);
//            cell = row.createCell(0, CellType.NUMERIC);
//            cell.setCellValue("STT");
//
//            cell = row.createCell(1, CellType.STRING);
//            cell.setCellValue("Code");
//
//            cell = row.createCell(2, CellType.STRING);
//            cell.setCellValue("Full Name");
//
//            cell = row.createCell(3, CellType.STRING);
//            cell.setCellValue("Account");
//
//            cell = row.createCell(4, CellType.STRING);
//            cell.setCellValue("Phone Number");
//
//            cell = row.createCell(5, CellType.STRING);
//            cell.setCellValue("Email");
//
//            cell = row.createCell(6, CellType.STRING);
//            cell.setCellValue("Address");
//
//            List<Employee> list = employeeService.getAll();
//
//            if (list != null) {
//                int s = list.size();
//                for (int i = 0; i < s; i++) {
//                    Employee hd = list.get(i);
//                    row = sheet.createRow(1 + i);
//
//                    cell = row.createCell(0, CellType.NUMERIC);
//                    cell.setCellValue(i + 1);
//
//                    cell = row.createCell(1, CellType.STRING);
//                    cell.setCellValue(hd.getCode());
//
//                    cell = row.createCell(2, CellType.STRING);
//                    cell.setCellValue(hd.getFullName());
//
//                    cell = row.createCell(3, CellType.STRING);
//                    cell.setCellValue(hd.getAccount().getAccount());
////
//                    cell = row.createCell(4, CellType.STRING);
//                    cell.setCellValue(hd.getAccount().getPhoneNumber());
////
//                    cell = row.createCell(5, CellType.STRING);
//                    cell.setCellValue(hd.getAccount().getEmail());
//
////                    cell = row.createCell(6, CellType.STRING);
////                    cell.setCellValue(new Da);
//
////                    cell = row.createCell(6, CellType.BOOLEAN);
////                    cell.setCellValue(hd.getGender().toString());
//
//                    cell = row.createCell(6, CellType.STRING);
//                    cell.setCellValue(hd.getAddress());
//
//
//                }
//                File e = new File("E:\\"+"FileEmployee"+date.format(getDate)+".xlsx");
//                try {
//                    FileOutputStream fis = new FileOutputStream(e);
//                    worbook.write(fis);
//                    fis.close();
//                } catch (FileNotFoundException x) {
//                    x.printStackTrace();
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }



}