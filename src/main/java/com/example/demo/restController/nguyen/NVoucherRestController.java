package com.example.demo.restController.nguyen;

import com.example.demo.entity.Voucher;
import com.example.demo.model.request.nguyen.VoucherRequest;
import com.example.demo.model.response.nguyen.CustomerVoucherStatsDTO;
import com.example.demo.model.response.nguyen.VoucherStatistics;
import com.example.demo.service.nguyen.NVoucherService;
import com.example.demo.model.response.nguyen.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/voucher")
public class NVoucherRestController {

    @Autowired
    NVoucherService NVoucherService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(NVoucherService.getAllVoucher());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(NVoucherService.getVoucherById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Voucher voucher) {
//        return ResponseEntity.ok(voucherService.createVoucher(voucher));
        return null;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody Voucher voucher, @PathVariable Long id) {
//        return ResponseEntity.ok(voucherService.updateVoucher(voucher, id));
        return null;
    }

    @GetMapping("/page")
    public PaginationResponse<Voucher> getVouchers(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer applyfor,
            @RequestParam(required = false) Integer discountType,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) Date endDate,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber) {

        // Kiểm tra và thêm một ngày vào startDate nếu không null
        if (startDate != null) {
            LocalDate localDate = startDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            localDate = localDate.plusDays(1);
            startDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        Pageable pageable = PageRequest.of(pageNumber, 5);
        Page<Voucher> page = NVoucherService.findVouchers(code, name, applyfor,
                discountType, startDate, endDate, status, pageable);
        return new PaginationResponse<>(page);
    }

    @PostMapping("/saveVoucher")
    public ResponseEntity<?> saveVoucher(@RequestBody VoucherRequest voucherRequest) {
        System.out.println(voucherRequest.getVoucher());
        System.out.println(voucherRequest.getCustomerTypeList().size());
        System.out.println(voucherRequest.getCustomerTypeList());
        return ResponseEntity.ok(NVoucherService
                .createVoucher(voucherRequest.getVoucher(), voucherRequest.getCustomerTypeList()));
//        return null;
    }

    @PutMapping("/updateVoucher/{id}")
    public ResponseEntity<?> updateVoucher(@RequestBody VoucherRequest voucherRequest,
                                           @PathVariable Long id) {
        System.out.println(voucherRequest.getVoucher());
        System.out.println(voucherRequest.getCustomerTypeList().size());
        System.out.println(voucherRequest.getCustomerTypeList());
//        return null;
        return ResponseEntity.ok(NVoucherService
                .updateVoucher(id, voucherRequest.getVoucher(),
                        voucherRequest.getCustomerTypeList()));
    }

    @GetMapping("/{voucherId}/statistics")
    public VoucherStatistics getVoucherStatistics(@PathVariable Long voucherId) {
        return NVoucherService.calculateVoucherStatistics(voucherId);
    }

    @GetMapping("/{voucherId}/stats")
    public Page<CustomerVoucherStatsDTO> getCustomerVoucherStats(
            @PathVariable Long voucherId,
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, 5);
        return NVoucherService.getCustomerVoucherStats(voucherId, pageable);
    }
}
