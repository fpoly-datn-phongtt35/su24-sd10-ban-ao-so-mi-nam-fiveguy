package com.example.demo.restController.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillDetail;
import com.example.demo.model.request.nguyen.BillRequest;
import com.example.demo.service.nguyen.NBillDetailService;
import com.example.demo.service.nguyen.NBillService;
import com.example.demo.service.nguyen.NPaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

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
        Pageable pageable = PageRequest.of(page, size);
        return billService
                .searchBills(code, customerName, phoneNumber, typeBill, startDate, endDate, status,
                        pageable);
    }

    @PutMapping("/shipUpdate/{id}")
    public Bill updateShipmentDetail(@RequestBody Bill bill, @PathVariable Long id) {
        return billService.updateShipmentDetail(bill, id);
    }

    @PutMapping("/billStatusUpdate/{id}")
    public Bill updateBillStatusAndSaveBillHistory(@RequestBody BillRequest billRequest,
                                                   @PathVariable Long id) {
//        System.out.println(billRequest.getBill().getStatus());
//        System.out.println(billRequest.getBillHistory());
//        return null;
        return billService
                .updateStatusAndBillStatus(billRequest.getBill(), id, billRequest.getBillHistory());
    }

    @PostMapping("/{billId}/details")
    public ResponseEntity<BillDetail> addProductDetailToBill(
            @PathVariable Long billId,
            @RequestParam Long productDetailId,
            @RequestParam Integer quantity,
            @RequestParam BigDecimal price,
            @RequestParam BigDecimal promotionalPrice) {

        BillDetail billDetail = billDetailService
                .addProductDetailToBill(billId, productDetailId, quantity, price, promotionalPrice);
        return ResponseEntity.ok(billDetail);
    }

    @DeleteMapping("/details/{billDetailId}")
    public ResponseEntity<Void> removeProductDetailFromBill(@PathVariable Long billDetailId) {
        billDetailService.removeProductDetailFromBill(billDetailId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/details/{billDetailId}/quantity")
    public ResponseEntity<BillDetail> updateBillDetailQuantity(
            @PathVariable Long billDetailId,
            @RequestParam Integer newQuantity) {

        BillDetail updatedBillDetail = billDetailService
                .updateBillDetailQuantity(billDetailId, newQuantity);
        return ResponseEntity.ok(updatedBillDetail);
    }

    @GetMapping("/{billId}/paymentStatus")
    public ResponseEntity<?> getAllPaymentStatusByBillId(@PathVariable Long billId){
        return ResponseEntity.ok(paymentStatusService.getAllByBillId(billId));
    }
}
