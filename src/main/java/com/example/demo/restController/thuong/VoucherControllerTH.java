package com.example.demo.restController.thuong;

import com.example.demo.model.response.thuong.VoucherResponseTH;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.service.thuong.VoucherServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/voucher-th")
public class VoucherControllerTH {
    @Autowired
    private VoucherServiceTH voucherCommonService;

    @Autowired
    private SCCustomerService SCCustomerService;

    @GetMapping("/customer/vouchers")
    public List<VoucherResponseTH> getVouchersForCustomer(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "search", required = false) String search) {
            return voucherCommonService.getVouchersForCustomer(id, search);
    }

}
