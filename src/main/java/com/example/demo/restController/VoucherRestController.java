package com.example.demo.restController;

import com.example.demo.entity.Voucher;
import com.example.demo.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/voucher")
public class VoucherRestController {

    @Autowired
    VoucherService voucherService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(voucherService.getAllVoucher());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(voucherService.getVoucherById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Voucher voucher){
        return ResponseEntity.ok(voucherService.createVoucher(voucher));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody Voucher voucher, @PathVariable Long id){
        return ResponseEntity.ok(voucherService.updateVoucher(voucher, id));
    }
}
