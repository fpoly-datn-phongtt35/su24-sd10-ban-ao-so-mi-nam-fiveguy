package com.example.demo.restController.nguyen;


import com.example.demo.entity.Bill;
import com.example.demo.security.service.SCAccountService;
import com.example.demo.service.nguyen.NBillDetailService;
import com.example.demo.service.nguyen.NBillService;
import com.example.demo.service.nguyen.NPaymentStatusService;
import com.example.demo.service.nguyen.NSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/sell")
public class NSellRestController {


    @Autowired
    NSellService sellService;

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
        return ResponseEntity.ok(sellService.getAllBillSell());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sellService.getById(id));
    }

    @PostMapping("/createBill")
    public ResponseEntity<?> createBillSell(
            @RequestHeader("Authorization") String token, @RequestBody Bill bill){
        return ResponseEntity.ok(sellService.createBillSell(bill, null));
    }

    @DeleteMapping("/removeBill/{id}")
    public void removeBillSell(
            @RequestHeader("Authorization") String token, @PathVariable Long id){
        sellService.removeBillSell(id);
    }
}
