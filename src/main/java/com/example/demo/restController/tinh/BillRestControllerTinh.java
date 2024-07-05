package com.example.demo.restController.tinh;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Employee;
import com.example.demo.service.tinh.BillServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/bill-tinh")
public class BillRestControllerTinh {
    @Autowired
    BillServiceTinh billServiceTinh;

    @GetMapping("")
    public ResponseEntity<List<Bill>> getAll(){
        List<Bill> bills = billServiceTinh.getAll();
        return ResponseEntity.ok(bills);
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody Bill bill) {
        try {
            Bill createdBill = billServiceTinh.create(bill);
            return ResponseEntity.status(HttpStatus.CREATED).body(billServiceTinh);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
