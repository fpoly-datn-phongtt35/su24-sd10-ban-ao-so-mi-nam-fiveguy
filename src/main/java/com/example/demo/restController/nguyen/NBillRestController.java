package com.example.demo.restController.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.service.nguyen.NBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/bill")
public class NBillRestController {

    @Autowired
    NBillService NBillService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(NBillService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(NBillService.getById(id));
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
        return NBillService
                .searchBills(code, customerName, phoneNumber, typeBill, startDate, endDate, status,
                        pageable);
    }
}
