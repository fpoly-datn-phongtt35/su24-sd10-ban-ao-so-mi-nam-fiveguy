package com.example.demo.restController.nguyen;

import com.example.demo.service.nguyen.CustomerVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/customerVoucher")
public class CustomerVocherRestController {

    @Autowired
    CustomerVoucherService customerVoucherService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(customerVoucherService.getAll());
    }

    @GetMapping("/voucher/{id}/customers")
    public ResponseEntity<?> getAllByVoucherId(@PathVariable Long id){
        return ResponseEntity.ok(customerVoucherService.findCustomersByVoucherId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(customerVoucherService.getById(id));
    }
}
