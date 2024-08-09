package com.example.demo.restController.nguyen;

import com.example.demo.entity.Voucher;
import com.example.demo.model.request.nguyen.VoucherRequest;
import com.example.demo.model.response.nguyen.CustomerVoucherStatsDTO;
import com.example.demo.model.response.nguyen.VoucherStatistics;
import com.example.demo.security.service.SCAccountService;
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
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/voucher")
public class NVoucherRestController {

    @Autowired
    NVoucherService voucherService;

    @Autowired
    private SCAccountService accountService;

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
            @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") @RequestParam(required = false) Date startDate,
            @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") @RequestParam(required = false) Date endDate,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber) {

        // Kiểm tra và thêm một ngày vào startDate nếu không null
//        if (startDate != null) {
//            LocalDate localDate = startDate.toInstant()
//                    .atZone(ZoneId.systemDefault())
//                    .toLocalDate();
//            localDate = localDate.plusDays(1);
//            startDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        }

        Pageable pageable = PageRequest.of(pageNumber, 5);
        Page<Voucher> page = voucherService.findVouchers(code, name, applyfor,
                discountType, startDate, endDate, status, pageable);
        return new PaginationResponse<>(page);
    }

    @PostMapping("/saveVoucher")
    public ResponseEntity<?> saveVoucher(@RequestHeader("Authorization") String token,
                                         @RequestBody VoucherRequest voucherRequest) {
        System.out.println(voucherRequest.getVoucher());
        System.out.println(voucherRequest.getCustomerTypeList().size());
        System.out.println(voucherRequest.getCustomerTypeList());

        Optional<String> fullName = accountService.getFullNameByToken(token);

        voucherRequest.getVoucher().setCreatedBy(fullName.get());

        return ResponseEntity.ok(voucherService
                .createVoucher(voucherRequest.getVoucher(), voucherRequest.getCustomerTypeList()));
    }

    @PutMapping("/updateVoucher/{id}")
    public ResponseEntity<?> updateVoucher(@RequestHeader("Authorization") String token,
                                           @RequestBody VoucherRequest voucherRequest,
                                           @PathVariable Long id) {
        System.out.println(voucherRequest.getVoucher());
        System.out.println(voucherRequest.getCustomerTypeList().size());
        System.out.println(voucherRequest.getCustomerTypeList());

        Optional<String> fullName = accountService.getFullNameByToken(token);

        voucherRequest.getVoucher().setUpdatedBy(fullName.get());

        return ResponseEntity.ok(voucherService
                .updateVoucher(id, voucherRequest.getVoucher(),
                        voucherRequest.getCustomerTypeList()));
    }

    @GetMapping("/{voucherId}/statistics")
    public VoucherStatistics getVoucherStatistics(@PathVariable Long voucherId) {
        return voucherService.calculateVoucherStatistics(voucherId);
    }

    @GetMapping("/{voucherId}/stats")
    public Page<CustomerVoucherStatsDTO> getCustomerVoucherStats(
            @PathVariable Long voucherId,
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, 5);
        return voucherService.getCustomerVoucherStats(voucherId, pageable);
    }

    @GetMapping("/findAllVoucherCanUse/{billId}")
    public List<Voucher> findAllVoucherCanUse(@PathVariable Long billId){
        return voucherService.findAllVoucherCanUse(billId);
    }
}
