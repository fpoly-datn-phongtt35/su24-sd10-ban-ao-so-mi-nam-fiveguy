package com.example.demo.restController.nguyen;

import com.example.demo.entity.Voucher;
import com.example.demo.service.nguyen.VoucherService;
import com.example.demo.untility.nguyen.PaginationResponse;
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
public class VoucherRestController {

    @Autowired
    VoucherService voucherService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(voucherService.getAllVoucher());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(voucherService.getVoucherById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Voucher voucher) {
        return ResponseEntity.ok(voucherService.createVoucher(voucher));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody Voucher voucher, @PathVariable Long id) {
        return ResponseEntity.ok(voucherService.updateVoucher(voucher, id));
    }

    @GetMapping("/page")
    public PaginationResponse<Voucher> getVouchers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
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

        Pageable pageable = PageRequest.of(pageNumber, 4);
        Page<Voucher> page = voucherService.findVouchers(name, code, discountType, startDate, endDate, status, pageable);
        return new PaginationResponse<>(page);
    }
}
