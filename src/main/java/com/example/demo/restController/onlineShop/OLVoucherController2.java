package com.example.demo.restController.onlineShop;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Voucher;
import com.example.demo.model.response.common.VoucherDTO;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.service.common.VoucherCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/home")
public class OLVoucherController2 {

    @Autowired
    private VoucherCommonService voucherCommonService;

    @Autowired
    private SCCustomerService SCCustomerService;

    @GetMapping("/vouchers")
    public List<Voucher> getVouchers() {
        return voucherCommonService.getAllVouchersByStatusAndApplyFor();
    }

    @GetMapping("/customer/vouchers")
    public List<VoucherDTO> getVouchersForCustomer(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "search", required = false) String search) {
        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);
        if (customer.isPresent()) {
            return voucherCommonService.getVouchersForCustomer(customer.get(), search);
        }
        return null;
    }



//    @GetMapping("/bestVoucher")
//    public Voucher getBestVoucher(@RequestParam double totalAmount,@RequestHeader("Authorization") String token) {
//        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);
//        if (customer.isPresent()) {
//            return olVoucherService2.selectBestVoucher(totalAmount,customer.get());
//
//        }
//        return null;
//
//    }










}
