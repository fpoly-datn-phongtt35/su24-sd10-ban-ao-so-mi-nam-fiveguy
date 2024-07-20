package com.example.demo.restController.tinh;

import com.example.demo.entity.BillHistory;
import com.example.demo.entity.Employee;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.tinh.BillHistoryRepositoryTinh;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.tinh.BillHistoryServiceTinh;
import com.example.demo.untility.tinh.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/bill-history-tinh")
public class BillHistoryRestControllerTinh {
    @Autowired
    BillHistoryServiceTinh billHistoryServiceTinh;

    @Autowired
    BillHistoryRepositoryTinh billHistoryRepositoryTinh;

    @Autowired
    SCEmployeeService scEmployeeService;

    @GetMapping("")
    public ResponseEntity<List<BillHistory>> getAll(){
        List<BillHistory> billHistories= billHistoryServiceTinh.getAll();
        return ResponseEntity.ok(billHistories);
    }

    @PostMapping(value = "/save", produces = "text/plain")
    public BillHistory create(@RequestHeader("Authorization")String token,@RequestBody BillHistory bill){
        BillHistory bill1 = new BillHistory();
        Optional<Employee> employee = scEmployeeService.getEmployeeByToken(token);

        bill1.setBill(bill.getBill());
        bill1.setDescription(bill.getDescription());
        bill1.setCreatedAt(new Date());
        bill1.setCreatedBy(employee.get().getFullName());
        bill1.setReason(bill.getReason());
        bill1.setStatus(1);

        return billHistoryRepositoryTinh.save(bill1);

    }

    @GetMapping("/page")
    public PaginationResponse<BillHistory> getHistory(
            @RequestParam(required = false) Date createdAt,
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, size, sort);
        Page<BillHistory> page = billHistoryServiceTinh.findHistory(createdAt, createdBy, type, status, pageable);
        return new PaginationResponse<>(page);
    }


}
