package com.example.demo.restController.tinh;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.repository.tinh.BillDetailRepositoryTinh;
import com.example.demo.service.tinh.BillDetailServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/billdetail-tinh")
public class BillDetailRestControllerTinh {

    @Autowired
    BillDetailServiceTinh billDetailServiceTinh;

    @Autowired
    BillDetailRepositoryTinh billDetailRepositoryTinh;

    //Tổng sản phẩm bán được
    @GetMapping("/san-pham-ban-ra-ngay/{sl}")
    public ResponseEntity<Integer> sanPhamBanDuocNgay(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        Integer customers = billDetailRepositoryTinh.sanPhamBanDuocNgay(sl);
        return ResponseEntity.ok(customers);
    }
    @GetMapping("/san-pham-ban-ra-tuan/{sl}")
    public ResponseEntity<Integer> sanPhamBanDuocTuan(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        Integer customers = billDetailRepositoryTinh.sanPhamBanDuocWeed(sl);
        return ResponseEntity.ok(customers);
    }
    @GetMapping("/san-pham-ban-ra-thang/{sl}")
    public ResponseEntity<Integer> sanPhamBanDuocThang(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        Integer customers = billDetailRepositoryTinh.sanPhamBanDuocMonth(sl);
        return ResponseEntity.ok(customers);
    }
    @GetMapping("/san-pham-ban-ra-nam/{sl}")
    public ResponseEntity<Integer> sanPhamBanDuocNam(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        Integer customers = billDetailRepositoryTinh.sanPhamBanDuocNam(sl);
        return ResponseEntity.ok(customers);
    }


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
