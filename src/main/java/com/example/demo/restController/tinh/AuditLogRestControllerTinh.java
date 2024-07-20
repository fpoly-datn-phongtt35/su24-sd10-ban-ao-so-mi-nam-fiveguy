package com.example.demo.restController.tinh;

import com.example.demo.entity.AuditLogs;
import com.example.demo.entity.BillHistory;
import com.example.demo.entity.Employee;
import com.example.demo.repository.tinh.AuditLogRepositoryTinh;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.tinh.AuditLogServiceTinh;
import com.example.demo.untility.tinh.PaginationResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/audit-log")
public class AuditLogRestControllerTinh {
    @Autowired
    AuditLogServiceTinh auditLogServiceTinh;

    @Autowired
    AuditLogRepositoryTinh auditLogRepositoryTinh;

    @Autowired
    SCEmployeeService scEmployeeService;

    @GetMapping("")
    public ResponseEntity<List<AuditLogs>> getAll() {
        List<AuditLogs> customers = auditLogServiceTinh.getAll();
        return ResponseEntity.ok(customers);
    }


    @PostMapping(value = "/save", produces = "application/json")
    public AuditLogs create(@RequestHeader("Authorization")String token, @RequestBody AuditLogs bill){
        AuditLogs bill1 = new AuditLogs();
        Optional<Employee> employee = scEmployeeService.getEmployeeByToken(token);

//        bill1.setEmpCode(employee.get().getCode());
//        bill1.setImplementer(employee.get().getFullName());
//        bill1.setActionType("Tạo hoa đơn");
//        bill1.setDetailedAction("Nhân viên " + employee.get().getFullName() + " dã tạo hóa đơn ");
        bill1.setEmpCode(bill.getEmpCode());
        bill1.setImplementer(bill.getImplementer());
        bill1.setActionType(bill.getActionType());
        bill1.setDetailedAction(bill.getDetailedAction());
        bill1.setTime(new Date());
        bill1.setStatus(1);

        return auditLogRepositoryTinh.save(bill1);

    }

    //Xuất file excel lich sử nhân viên
    @GetMapping("/exce-lich-su")
    public void fileExcelAuditLog() {
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
            cell.setCellValue("Người thực hiên");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("Loại hàng đông");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue("Chi tiết hàng động");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue("Thời gian");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue("Trạng thái");
            cell.setCellStyle(headerStyle);

            // Adjust column widths
            sheet.setColumnWidth(0, 256 * 10); // STT column
            sheet.setColumnWidth(1, 256 * 15); // Mã column
            sheet.setColumnWidth(2, 256 * 25); // Người thực hiên column
            sheet.setColumnWidth(3, 256 * 20); // Loại hành động column
            sheet.setColumnWidth(4, 256 * 30); // Chi tiết hd column
            sheet.setColumnWidth(5, 256 * 20); // time column
            sheet.setColumnWidth(6, 256 * 10); // status colum

            List<AuditLogs> list = auditLogServiceTinh.getAll();

            if (list != null) {
                int s = list.size();
                for (int i = 0; i < s; i++) {
                    AuditLogs hd = list.get(i);
                    row = sheet.createRow(1 + i);

                    cell = row.createCell(0, CellType.NUMERIC);
                    cell.setCellValue(i + 1);

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue(hd.getEmpCode());

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue(hd.getImplementer());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue(hd.getActionType());
//
                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellValue(hd.getDetailedAction());
//
                    cell = row.createCell(5, CellType.STRING);
                    if (hd.getTime() != null) {
                        // Assuming hd.getBirthDate() returns a java.util.Date object
                        Date birthDate = hd.getTime();
                        CellStyle dateCellStyle = workbook.createCellStyle();
                        CreationHelper createHelper = workbook.getCreationHelper();
                        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
                        cell.setCellStyle(dateCellStyle);
                        cell.setCellValue(birthDate);
                    } else {
                        cell.setCellValue("Invalid date");
                    }

                    cell = row.createCell(6, CellType.NUMERIC);
                    cell.setCellValue(hd.getStatus());

                }
                File e = new File("E:\\"+"AuditLogs"+" "+date.format(getDate)+".xlsx");
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

    @GetMapping("/auditlog-page")
    public PaginationResponse<AuditLogs> getEmployees(
            @RequestParam(required = false) String implementer,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) Date time,
            @RequestParam(required = false) String detailAction,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, size,sort);
        Page<AuditLogs> page = auditLogServiceTinh.findAuditLog(implementer, code, actionType, time, detailAction, status, pageable);
        return new PaginationResponse<>(page);
    }
}
