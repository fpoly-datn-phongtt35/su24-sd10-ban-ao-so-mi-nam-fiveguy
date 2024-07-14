package com.example.demo.restController.onlineShop;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Voucher;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.service.common.OLVoucherService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/home")
public class OLVoucherController2 {

    @Autowired
    private OLVoucherService2 olVoucherService2;

    @Autowired
    private SCCustomerService SCCustomerService;

    @GetMapping("/vouchers")
    public List<Voucher> getVouchers() {
        return olVoucherService2.getAllVouchersByStatusAndApplyFor();
    }

    @GetMapping("/customer/vouchers")
    public List<Voucher> getVouchersForCustomer(@RequestHeader("Authorization") String token) {
        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);
        if (customer.isPresent()) {
            return olVoucherService2.   getVouchersForCustomer(customer.get());
        }
        return null;
    }


    @GetMapping("/bestVoucher")
    public Voucher getBestVoucher(@RequestParam double totalAmount,@RequestHeader("Authorization") String token) {
        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);
        if (customer.isPresent()) {
            return olVoucherService2.selectBestVoucher(totalAmount,customer.get());

        }
        return null;

    }










}
