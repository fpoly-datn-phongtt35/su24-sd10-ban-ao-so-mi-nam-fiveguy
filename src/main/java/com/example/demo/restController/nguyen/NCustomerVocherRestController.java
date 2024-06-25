package com.example.demo.restController.nguyen;

import com.example.demo.service.nguyen.NCustomerVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/customerVoucher")
public class NCustomerVocherRestController {

    @Autowired
    NCustomerVoucherService NCustomerVoucherService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(NCustomerVoucherService.getAll());
    }

    @GetMapping("/voucher/{id}/customers")
    public ResponseEntity<?> getAllByVoucherId(@PathVariable Long id){
        return ResponseEntity.ok(NCustomerVoucherService.findCustomersByVoucherId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(NCustomerVoucherService.getById(id));
    }
}
