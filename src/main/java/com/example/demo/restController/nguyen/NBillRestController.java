package com.example.demo.restController.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.entity.ReturnOrder;
import com.example.demo.entity.Voucher;
import com.example.demo.model.request.nguyen.BillRequest;
import com.example.demo.security.service.SCAccountService;
import com.example.demo.service.nguyen.NBillDetailService;
import com.example.demo.service.nguyen.NBillService;
import com.example.demo.service.nguyen.NPaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/bill")
public class NBillRestController {

    @Autowired
    NBillService billService;

    @Autowired
    NBillDetailService billDetailService;

    @Autowired
    NPaymentStatusService paymentStatusService;

    @Autowired
    private SCAccountService accountService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(billService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getById(id));
    }

    @GetMapping("/page")
    public Page<Bill> getBills(@RequestParam(required = false) String code,
                               @RequestParam(required = false) String customerName,
                               @RequestParam(required = false) String phoneNumber,
                               @RequestParam(required = false) Integer typeBill,
                               @RequestParam(required = false) Date startDate,
                               @RequestParam(required = false) Date endDate,
                               @RequestParam(required = false) Integer status,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return billService
                .searchBills(code, customerName, phoneNumber, typeBill, startDate, endDate, status,
                        pageable);
    }

    @PutMapping("/shipUpdate/{id}")
    public Bill updateShipmentDetail(@RequestHeader("Authorization") String token,
                                     @RequestBody Bill bill, @PathVariable Long id) {

        Optional<String> fullName = accountService.getFullNameByToken(token);

        return billService.updateShipmentDetail(bill, id, fullName.get());
    }

    @PutMapping("/shippingFeeUpdate/{id}")
    public Bill updateShippingFee(@RequestHeader("Authorization") String token,
                                  @PathVariable Long id, @RequestBody BigDecimal shippingFee) {

        return billService.updateShippingFee(id, shippingFee);
    }

    @PutMapping("/billStatusUpdate/{id}")
    public Bill updateBillStatusAndSaveBillHistory(
            @RequestHeader("Authorization") String token, @RequestBody BillRequest billRequest,
            @PathVariable Long id) {

        Optional<String> fullName = accountService.getFullNameByToken(token);
        billRequest.getBillHistory().setCreatedBy(fullName.get());

        return billService
                .updateStatusAndBillStatus(billRequest.getBill(), id, billRequest.getBillHistory());
    }

    @PostMapping("/{billId}/details")
    public ResponseEntity<BillDetail> addProductDetailToBill(
            @RequestHeader("Authorization") String token,
            @PathVariable Long billId,
            @RequestParam Long productDetailId,
            @RequestParam Integer quantity,
            @RequestParam BigDecimal price,
            @RequestParam BigDecimal promotionalPrice) {

        Optional<String> fullName = accountService.getFullNameByToken(token);

        BillDetail billDetail = billDetailService
                .addProductDetailToBill(billId, productDetailId, quantity, price, promotionalPrice);
        return ResponseEntity.ok(billDetail);
    }

    @DeleteMapping("/details/{billDetailId}")
    public ResponseEntity<Void> removeProductDetailFromBill(
            @RequestHeader("Authorization") String token, @PathVariable Long billDetailId) {

        Optional<String> fullName = accountService.getFullNameByToken(token);

        billDetailService.removeProductDetailFromBill(billDetailId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/details/{billDetailId}/quantity")
    public ResponseEntity<BillDetail> updateBillDetailQuantity(
            @RequestHeader("Authorization") String token,
            @PathVariable Long billDetailId,
            @RequestParam Integer newQuantity) {

        Optional<String> fullName = accountService.getFullNameByToken(token);

        BillDetail updatedBillDetail = billDetailService
                .updateBillDetailQuantity(billDetailId, newQuantity);
        return ResponseEntity.ok(updatedBillDetail);
    }

    @GetMapping("/{billId}/paymentStatus")
    public ResponseEntity<?> getAllPaymentStatusByBillId(@PathVariable Long billId) {
        return ResponseEntity.ok(paymentStatusService.getAllByBillId(billId));
    }

    @PutMapping("/{billId}/updateStatusPayment")
    public ResponseEntity<?> updateStatusPayment(@PathVariable Long billId,
                                                 @RequestBody BigDecimal paymentAmount) {
        return ResponseEntity.ok(paymentStatusService.updateStatusPayment(billId, paymentAmount));
    }

    @GetMapping("/{billId}/checkQuantity")
    public ResponseEntity<?> checkQuantity(@PathVariable Long billId) {
        return ResponseEntity.ok(billService.isQuantityExceedsProductDetail(billId));
    }

    //khong su dung - chua check lai
    @PostMapping("/{billId}/addReturnOrder")
    public ResponseEntity<?> addReturnOrderAndUpdateBill(
            @RequestHeader("Authorization") String token,
            @RequestBody List<ReturnOrder> returnOrdera) {
        return ResponseEntity.ok(null);
    }

    @PutMapping("/{billId}/setVoucherToBill")
    public ResponseEntity<?> setVoucherToBill(@PathVariable Long billId,
                                              @RequestBody Voucher voucher) {
        return ResponseEntity.ok(billService.setVoucherToBill(billId, voucher));
    }
}
