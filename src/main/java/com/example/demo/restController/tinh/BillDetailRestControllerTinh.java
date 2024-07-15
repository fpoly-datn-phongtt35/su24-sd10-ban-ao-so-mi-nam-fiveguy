package com.example.demo.restController.tinh;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.repository.tinh.BillDetailRepositoryTinh;
import com.example.demo.service.tinh.BillDetailServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/billdetail-tinh")
public class BillDetailRestControllerTinh {

    @Autowired
    BillDetailServiceTinh billDetailServiceTinh;

    @Autowired
    BillDetailRepositoryTinh billDetailRepositoryTinh;

    @GetMapping("")
    public ResponseEntity<List<BillDetail>> getAll(){
        List<BillDetail> bills = billDetailRepositoryTinh.findAll();
        return ResponseEntity.ok(bills);
    }

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody BillDetail bill) {
        try {
            BillDetail createdBill = billDetailServiceTinh.create(bill);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
