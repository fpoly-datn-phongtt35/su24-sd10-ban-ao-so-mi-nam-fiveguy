package com.example.demo.restController.tinh;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Employee;
import com.example.demo.repository.tinh.BillRepositoryTinh;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.tinh.BillServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("")
    public ResponseEntity<List<Bill>> getAll(){
        List<Bill> bills = billServiceTinh.getAll();
        return ResponseEntity.ok(bills);
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Bill bill) {
        try {
            Bill createdBill = billServiceTinh.create(bill);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/save", produces = "text/plain")
    public Bill createBill(@RequestHeader("Authorization")String token, Bill bill){
        Bill bill1 = new Bill();
        Optional<Employee> employee = scEmployeeService.getEmployeeByToken(token);
//        Employee employee1 = new Employee();
        String randomCode = generateRandomCode(6);
        bill1.setCode(randomCode);
        bill1.setEmployee(employee.get().getFullName());;
        bill1.setCreatedAt(new Date());
        bill1.setCustomer(bill.getCustomer());
        bill1.setTypeBill(1);
        bill1.setStatus(1);cv

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
}
