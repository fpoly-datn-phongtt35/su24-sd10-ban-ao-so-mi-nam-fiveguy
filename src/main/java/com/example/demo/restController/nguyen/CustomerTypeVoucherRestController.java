package com.example.demo.restController.nguyen;

import com.example.demo.service.nguyen.CustomerTypeVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/customerTypeVoucher")
public class CustomerTypeVoucherRestController {

    @Autowired
    CustomerTypeVoucherService customerTypeVoucherService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(customerTypeVoucherService.getAllCusomerTypeVouchers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(customerTypeVoucherService.getCustomerTypeVoucherById(id));
    }

    @GetMapping("/allCustomerType/{id}")
    public ResponseEntity<?> getAllCustomerTypeByVoucherId(@PathVariable Long id){
        return ResponseEntity.ok(customerTypeVoucherService.getAllByVoucherId(id));
    }
}
