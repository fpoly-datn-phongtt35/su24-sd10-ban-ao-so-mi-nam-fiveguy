package com.example.demo.restController.nguyen;

import com.example.demo.model.response.nguyen.BillDetailSummary;
import com.example.demo.service.nguyen.NBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/billDetail")
public class NBillDetailRestController {

    @Autowired
    NBillDetailService billDetailService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(billDetailService.getById(id));
    }

    @GetMapping("/getAllByBillId/{billId}")
    public ResponseEntity<?> getAllByBillId(@PathVariable Long billId) {
        return ResponseEntity.ok(billDetailService.getAllByBillId(billId));
    }

    @GetMapping("/{billId}/summary")
    public ResponseEntity<BillDetailSummary> getBillDetailSummary(@PathVariable Long billId) {
        BillDetailSummary summary = billDetailService.getBillDetailSummaryByBillId(billId);
        return ResponseEntity.ok(summary);
    }
}
