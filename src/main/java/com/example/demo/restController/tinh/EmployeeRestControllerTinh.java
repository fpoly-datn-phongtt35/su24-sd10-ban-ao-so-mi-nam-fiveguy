package com.example.demo.restController.tinh;

//import com.example.demo.entity.AccountEntity

import com.example.demo.entity.Account;
import com.example.demo.entity.Employee;
import com.example.demo.repository.tinh.EmployeeRepositoryTinh;
import com.example.demo.service.tinh.EmployeeServiceTinh;
import com.example.demo.untility.tinh.PaginationResponse;
import org.apache.poi.ss.usermodel.*;
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
public class EmployeeRestControllerTinh {
    @Autowired
    EmployeeServiceTinh employeeService;

//    @Autowired
//    AccountService accountService;

    @Autowired
    EmployeeRepositoryTinh employeeRepository;


    @GetMapping("")
    public ResponseEntity<List<Employee>> getAll() {
        List<Employee> customers = employeeService.getAll();
        return ResponseEntity.ok(customers);

    }

    //Get by employee từ trường account trong bảng account
    @GetMapping("/account/{account}")
    public ResponseEntity<Employee> getByAccount(@PathVariable String account) {
        Employee account1 = employeeService.getByAccount(account);
        return ResponseEntity.ok(account1);
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

    private static CellStyle cellStyleFormatNumber = null;
    @GetMapping("/excel")
    public void fileExcel() {
        try {
            XSSFWorkbook workbook  = new XSSFWorkbook();
            XSSFSheet sheet = workbook .createSheet("List of Employee");

            XSSFRow row = null;
            Cell cell = null;
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter getDate = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 13); // Set font size
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.LEFT); // Align left

            // Create cell style for data rows
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setAlignment(HorizontalAlignment.LEFT); // Align left


            row = sheet.createRow(0);
            cell = row.createCell(0, CellType.NUMERIC);
            cell.setCellValue("STT");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("Mã");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("Họ và tên");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("Tên tài khoản");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue("SĐT");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue("Email");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue("Giới tính");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(7, CellType.STRING);
            cell.setCellValue("Ngày sinh");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(8, CellType.STRING);
            cell.setCellValue("Chức vụ");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(9, CellType.STRING);
            cell.setCellValue("Address");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(10, CellType.STRING);
            cell.setCellValue("Trạng thái");
            cell.setCellStyle(headerStyle);


            // Adjust column widths
            sheet.setColumnWidth(0, 256 * 10); // STT column
            sheet.setColumnWidth(1, 256 * 15); // Mã column
            sheet.setColumnWidth(2, 256 * 25); // Họ và tên column
            sheet.setColumnWidth(3, 256 * 20); // Tên tài khoản column
            sheet.setColumnWidth(4, 256 * 15); // SĐT column
            sheet.setColumnWidth(5, 256 * 30); // Email column
            sheet.setColumnWidth(6, 256 * 10); // Giới tính column
            sheet.setColumnWidth(7, 256 * 15); // Ngày sinh column
            sheet.setColumnWidth(8, 256 * 20); // Chức vụ column
            sheet.setColumnWidth(9, 256 * 30); // Địa chỉ column
            sheet.setColumnWidth(10, 256 * 15); // Trạng thái column


            List<Employee> list = employeeService.getAll();

            if (list != null) {
                int s = list.size();
                for (int i = 0; i < s; i++) {
                    Employee hd = list.get(i);
                    row = sheet.createRow(1 + i);

                    cell = row.createCell(0, CellType.NUMERIC);
                    cell.setCellValue(i + 1);

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue(hd.getCode());

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue(hd.getFullName());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue(hd.getAccount().getAccount());
//
                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellValue(hd.getAccount().getPhoneNumber());
//
                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellValue(hd.getAccount().getEmail());

                    cell = row.createCell(6, CellType.BOOLEAN);
                    cell.setCellValue(hd.getGender() ? "Nam" : "Nữ");

                    cell = row.createCell(7, CellType.STRING);
                    if (hd.getBirthDate() != null) {
                        // Assuming hd.getBirthDate() returns a java.util.Date object
                        Date birthDate = hd.getBirthDate();
                        CellStyle dateCellStyle = workbook.createCellStyle();
                        CreationHelper createHelper = workbook.getCreationHelper();
                        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
                        cell.setCellStyle(dateCellStyle);
                        cell.setCellValue(birthDate);
                    } else {
                        cell.setCellValue("Invalid date");
                    }


                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellValue(hd.getAccount().getRole().getFullName());

                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellValue(hd.getAddress());

                    cell = row.createCell(10, CellType.NUMERIC);
                    cell.setCellValue(hd.getStatus());

                }
                File e = new File("E:\\"+"FileEmployee"+" "+date.format(getDate)+".xlsx");
                try {
                    FileOutputStream fis = new FileOutputStream(e);
                    workbook .write(fis);
                    fis.close();
                } catch (FileNotFoundException x) {
                    x.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }




}