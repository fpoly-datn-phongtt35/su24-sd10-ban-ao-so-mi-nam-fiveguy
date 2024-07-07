package com.example.demo.restController.Customer;

import com.example.demo.entity.Customer;
import com.example.demo.repository.Customer.CustomerRepositoryH;
import com.example.demo.service.Customer.CustomerServiceH;
import com.example.demo.untility.customer.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/customer")

public class CustomerRestControllerH {

    @Autowired
    CustomerServiceH customerServiceH;

//    @Autowired
//    AccountServiceH accountServiceH;

    @Autowired
    CustomerRepositoryH customerRepositoryH;


    @GetMapping("")
    public ResponseEntity<List<Customer>> getAll() {
        List<Customer> customers = customerServiceH.getAll();
        return ResponseEntity.ok(customers);

    }

    //get customer status =1
    @GetMapping("/status1")
    public ResponseEntity<List<Customer>> getAllStatusDangLam() {
        List<Customer> customer = customerServiceH.getAllStatusDangLam();
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id) {
        Customer customer = customerServiceH.getById(id);
        return ResponseEntity.ok(customer);
    }
    @GetMapping("/search-status/{id}")
    public ResponseEntity<List<Customer>> getAllstatus(@PathVariable Integer id) {
        List<Customer> customer =  customerServiceH.getAllStatus(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/update-status-khach-hang/{id}")
    public void updateStatus(@PathVariable Long id){
        customerRepositoryH.updateStatusCustomer(id);
    }

    //Thêm Customer
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Customer customers) {
        try {
            Customer createdCustomer = customerServiceH.create(customers);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // delete Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerServiceH.delete(id);
        return ResponseEntity.noContent().build();
    }

    //update customer
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer customers) {
        customerServiceH.update(id, customers);
        if (customers != null) {
            return ResponseEntity.ok(customers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/status/{id}")
    public ResponseEntity<Customer> updatestatus(@PathVariable Long id, @RequestBody Customer customers) {
        customerServiceH.updateStatus(id, customers);
        if (customers != null) {
            return ResponseEntity.ok(customers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//    @GetMapping("/page")
//    public PaginationResponse<Customer> getCustomer(
//            @RequestParam(required = false) String fullName,
//            @RequestParam(required = false) String code,
//            @RequestParam(required = false) String avatar,
//            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) Date birthDate,
//            @RequestParam(required = false) Boolean gender,
//            @RequestParam(required = false) String account,
//            @RequestParam(required = false) String email,
//            @RequestParam(required = false) String phoneNumber,
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) Integer status,
////            @RequestParam(defaultValue = "0") int page,
////            @RequestParam(defaultValue = "5") int size,
////            @RequestParam(defaultValue = "id") String sortField,
//            @RequestParam(required = true, defaultValue = "0") Integer pageNumber) {
//        Pageable pageable = PageRequest.of(pageNumber, 5);
//        Page<Customer> page = customerServiceH.findCustomer(fullName, code, avatar, birthDate, gender, account, email, phoneNumber, name, status, pageable);
//        return new PaginationResponse<>(page);
//    }
//    private static CellStyle cellStyleFormatNumber = null;
//    @GetMapping("/excel")
//    public void fileExcel() {
//        try {
//            XSSFWorkbook workbook  = new XSSFWorkbook();
//            XSSFSheet sheet = workbook .createSheet("List of Employee");
//
//            XSSFRow row = null;
//            Cell cell = null;
//            LocalDateTime date = LocalDateTime.now();
//            DateTimeFormatter getDate = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
//
//            // Create header style
//            CellStyle headerStyle = workbook.createCellStyle();
//            Font headerFont = workbook.createFont();
//            headerFont.setBold(true);
//            headerFont.setFontHeightInPoints((short) 13); // Set font size
//            headerStyle.setFont(headerFont);
//            headerStyle.setAlignment(HorizontalAlignment.LEFT); // Align left
//
//            // Create cell style for data rows
//            CellStyle dataCellStyle = workbook.createCellStyle();
//            dataCellStyle.setAlignment(HorizontalAlignment.LEFT); // Align left
//
//
//            row = sheet.createRow(0);
//            cell = row.createCell(0, CellType.NUMERIC);
//            cell.setCellValue("STT");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(1, CellType.STRING);
//            cell.setCellValue("Mã");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(2, CellType.STRING);
//            cell.setCellValue("Họ và tên");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(3, CellType.STRING);
//            cell.setCellValue("Tên tài khoản");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(4, CellType.STRING);
//            cell.setCellValue("SĐT");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(5, CellType.STRING);
//            cell.setCellValue("Email");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(6, CellType.STRING);
//            cell.setCellValue("Giới tính");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(7, CellType.STRING);
//            cell.setCellValue("Ngày sinh");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(8, CellType.STRING);
//            cell.setCellValue("Hạng khách Hàng");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(9, CellType.STRING);
//            cell.setCellValue("Trạng thái");
//            cell.setCellStyle(headerStyle);
//
//
//            // Adjust column widths
//            sheet.setColumnWidth(0, 256 * 10); // STT column
//            sheet.setColumnWidth(1, 256 * 15); // Mã column
//            sheet.setColumnWidth(2, 256 * 25); // Họ và tên column
//            sheet.setColumnWidth(3, 256 * 20); // Tên tài khoản column
//            sheet.setColumnWidth(4, 256 * 15); // SĐT column
//            sheet.setColumnWidth(5, 256 * 30); // Email column
//            sheet.setColumnWidth(6, 256 * 10); // Giới tính column
//            sheet.setColumnWidth(7, 256 * 15); // Ngày sinh column
//            sheet.setColumnWidth(8, 256 * 20); // Hạng khách hàng column
//            sheet.setColumnWidth(9, 256 * 30); // Trạng thái column
//
//
//            List<Customer> list = customerServiceH.getAll();
//
//            if (list != null) {
//                int s = list.size();
//                for (int i = 0; i < s; i++) {
//                    Customer hd = list.get(i);
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
//                    cell = row.createCell(6, CellType.BOOLEAN);
//                    cell.setCellValue(hd.getGender() ? "Nam" : "Nữ");
//
//                    cell = row.createCell(7, CellType.STRING);
//                    if (hd.getBirthDate() != null) {
//                        // Assuming hd.getBirthDate() returns a java.util.Date object
//                        Date birthDate = hd.getBirthDate();
//                        CellStyle dateCellStyle = workbook.createCellStyle();
//                        CreationHelper createHelper = workbook.getCreationHelper();
//                        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
//                        cell.setCellStyle(dateCellStyle);
//                        cell.setCellValue(birthDate);
//                    } else {
//                        cell.setCellValue("Invalid date");
//                    }
//
//
//                    cell = row.createCell(8, CellType.STRING);
//                    cell.setCellValue(hd.getAccount().getRole().getFullName());
//
//                    cell = row.createCell(9, CellType.STRING);
//                    cell.setCellValue(hd.getAddress());
//
//                    cell = row.createCell(10, CellType.NUMERIC);
//                    cell.setCellValue(hd.getStatus());
//
//                }
//                File e = new File("E:\\"+"FileCustomer"+" "+date.format(getDate)+".xlsx");
//                try {
//                    FileOutputStream fis = new FileOutputStream(e);
//                    workbook .write(fis);
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
