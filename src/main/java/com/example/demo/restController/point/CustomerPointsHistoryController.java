package com.example.demo.restController.point;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerPointsHistory;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.service.point.CustomerPointsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/home/points-history")
public class CustomerPointsHistoryController {

    @Autowired
    private CustomerPointsHistoryService service;

    @Autowired
    private SCCustomerService SCCustomerService;

    @GetMapping("/customer")
    public ResponseEntity<List<CustomerPointsHistory>> getPointsHistoryByCustomerId(@RequestHeader("Authorization") String token) {
        Optional<Customer> customerOpt = SCCustomerService.getCustomerByToken(token);

        if (customerOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<CustomerPointsHistory> pointsHistory = service.getPointsHistoryByCustomer(customerOpt.get());

        return new ResponseEntity<>(pointsHistory, HttpStatus.OK);
    }


//    @PostMapping("/add")
//    public ResponseEntity<CustomerPointsHistory> addPointsHistory(@RequestBody CustomerPointsHistory customerPointsHistory) {
//        CustomerPointsHistory savedPointsHistory = service.savePointsHistory(customerPointsHistory);
//        return new ResponseEntity<>(savedPointsHistory, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/update")
//    public ResponseEntity<CustomerPointsHistory> updatePointsHistory(@RequestBody CustomerPointsHistory customerPointsHistory) {
//        CustomerPointsHistory updatedPointsHistory = service.updatePointsHistory(customerPointsHistory);
//        return new ResponseEntity<>(updatedPointsHistory, HttpStatus.OK);
//    }

    @PostMapping("/addPointsFromBill")
    public ResponseEntity<String> addPointsFromBill(@RequestParam Long billId) {
        try {
            // Call the service method with the billId
            service.addPointsFromBill(billId);
            return ResponseEntity.ok("Points added successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}