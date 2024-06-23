package com.example.demo.restController.nguyen;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Voucher;
import com.example.demo.service.nguyen.NCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/customerN")
public class NCustomerRestController {

    @Autowired
    private NCustomerService customerService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping("/page")
    public Page<Customer> getCustomers(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long customerTypeId,
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, 5);
        return customerService.getCustomers(fullName, phoneNumber, email, customerTypeId, pageable);
    }
}
