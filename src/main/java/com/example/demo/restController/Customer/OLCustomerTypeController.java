package com.example.demo.restController.Customer;

import com.example.demo.entity.CustomerPointsHistory;
import com.example.demo.entity.CustomerType;
import com.example.demo.repository.Customer.OLCustomerTypeRepository;
import com.example.demo.service.point.CustomerPointsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/home/customer-types")
public class OLCustomerTypeController {

    @Autowired
    private OLCustomerTypeRepository customerTypeRepository;



    @GetMapping()
    public ResponseEntity<List<CustomerType>> getCustomerType() {
        List<CustomerType> pointsHistory = customerTypeRepository.findByStatusOrderByMinPointsAscMaxPointsDesc(1);
        return new ResponseEntity<>(pointsHistory, HttpStatus.OK);
    }


}