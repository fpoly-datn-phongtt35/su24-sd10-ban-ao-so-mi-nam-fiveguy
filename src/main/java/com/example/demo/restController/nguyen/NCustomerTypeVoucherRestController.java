package com.example.demo.restController.nguyen;

import com.example.demo.service.nguyen.NCustomerTypeVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/customerTypeVoucher")
public class NCustomerTypeVoucherRestController {

    @Autowired
    NCustomerTypeVoucherService NCustomerTypeVoucherService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(NCustomerTypeVoucherService.getAllCusomerTypeVouchers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(NCustomerTypeVoucherService.getCustomerTypeVoucherById(id));
    }

    @GetMapping("/allCustomerType/{id}")
    public ResponseEntity<?> getAllCustomerTypeByVoucherId(@PathVariable Long id){
        return ResponseEntity.ok(NCustomerTypeVoucherService.getAllByVoucherId(id));
    }
}
