package com.example.demo.restController.tinh;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillHistory;
import com.example.demo.entity.Employee;
import com.example.demo.repository.tinh.BillRepositoryTinh;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.tinh.BillServiceTinh;
import com.example.demo.untility.tinh.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/bill-tinh")
public class BillRestControllerTinh {
    @Autowired
    BillServiceTinh billServiceTinh;

    @Autowired
    SCEmployeeService scEmployeeService;

    @Autowired
    BillRepositoryTinh billRepositoryTinh;

//    @GetMapping("")
//    public ResponseEntity<List<Bill>> getAll(){
//        List<Bill> bills = billServiceTinh.getAll();
//        return ResponseEntity.ok(bills);
//    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Bill bill) {
        try {
            Bill createdBill = billServiceTinh.create(bill);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/save", produces = "application/json")
    public Bill createBill(@RequestHeader("Authorization") String token, @RequestBody Bill bill) {
        Bill bill1 = new Bill();
        Optional<Employee> employee = scEmployeeService.getEmployeeByToken(token);

        String randomCode = generateRandomCode(6);
        bill1.setCode(randomCode);
        bill1.setReciverName(bill.getReciverName());
        bill1.setDeliveryDate(bill.getDeliveryDate());
        bill1.setShippingFee(bill.getShippingFee());
        bill1.setAddress(bill.getAddress());
        bill1.setPhoneNumber(bill.getPhoneNumber());
        bill1.setTotalAmount(bill.getTotalAmount());
        bill1.setTotalAmountAfterDiscount(bill.getTotalAmountAfterDiscount());
        bill1.setPaymentMethod(bill.getPaymentMethod());
        bill1.setVoucher(bill.getVoucher());
        bill1.setNote(bill.getNote());
        bill1.setReason(bill.getReason());
        bill1.setEmployee(employee.get());
        bill1.setCreatedAt(new Date());
        bill1.setCustomer(bill.getCustomer());
        bill1.setTypeBill(1);
        bill1.setStatus(1);

        return billRepositoryTinh.save(bill1);
    }

    private String generateRandomCode(int length) {
        String uppercaseCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomCode = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(uppercaseCharacters.length());
            char randomChar = uppercaseCharacters.charAt(randomIndex);
            randomCode.append(randomChar);
        }

        return randomCode.toString();
    }

    @PutMapping("/update-bill-status-thanh-cong/{id}")
    public ResponseEntity<String> updateBillStatus(@PathVariable Long id) {
        try {
            billServiceTinh.updateBillStatus(id);
            return ResponseEntity.ok("Bill status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating Bill status");
        }
    }

    @GetMapping("/page")
    public PaginationResponse<Bill> getBillPage(
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, size, sort);
        Page<Bill> page = billRepositoryTinh.getAllBillChoThanhToan(pageable);
        return new PaginationResponse<>(page);
    }
}
