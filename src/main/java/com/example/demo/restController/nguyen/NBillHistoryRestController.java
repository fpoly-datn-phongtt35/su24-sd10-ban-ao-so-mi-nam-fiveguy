package com.example.demo.restController.nguyen;

import com.example.demo.entity.BillHistory;
import com.example.demo.service.nguyen.NBillHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/billHistory")
public class NBillHistoryRestController {

    @Autowired
    NBillHistoryService billHistoryService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(billHistoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(billHistoryService.getById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody BillHistory billHistory){
        return ResponseEntity.ok(billHistoryService.save(billHistory));
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<?> getByBillId(@PathVariable Long billId){
        List<BillHistory> billHistoryList = billHistoryService.getByBillId(billId);
        return ResponseEntity.ok(billHistoryList);
    }
}
