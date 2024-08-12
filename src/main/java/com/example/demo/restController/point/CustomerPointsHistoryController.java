package com.example.demo.restController.point;

import com.example.demo.entity.CustomerPointsHistory;
import com.example.demo.service.point.CustomerPointsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/points-history")
public class CustomerPointsHistoryController {

    @Autowired
    private CustomerPointsHistoryService service;



    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerPointsHistory>> getPointsHistoryByCustomerId(@PathVariable Long customerId) {
        List<CustomerPointsHistory> pointsHistory = service.getPointsHistoryByCustomerId(customerId);
        return new ResponseEntity<>(pointsHistory, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CustomerPointsHistory> addPointsHistory(@RequestBody CustomerPointsHistory customerPointsHistory) {
        CustomerPointsHistory savedPointsHistory = service.savePointsHistory(customerPointsHistory);
        return new ResponseEntity<>(savedPointsHistory, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<CustomerPointsHistory> updatePointsHistory(@RequestBody CustomerPointsHistory customerPointsHistory) {
        CustomerPointsHistory updatedPointsHistory = service.updatePointsHistory(customerPointsHistory);
        return new ResponseEntity<>(updatedPointsHistory, HttpStatus.OK);
    }
}