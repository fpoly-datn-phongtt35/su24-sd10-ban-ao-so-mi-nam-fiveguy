package com.example.demo.restController.onlineShop;

import com.example.demo.service.onlineShop.OLCustomerService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/customer")
public class OLCustomerController2 {


    @Autowired
    private OLCustomerService2 customerService;

    @GetMapping("/convert-points")
    public int convertAmountToPoints(@RequestParam BigDecimal totalAmount) {
        return customerService.convertAmountToPoints(totalAmount);
    }

    @PutMapping("/{id}/update-points")
    public void updateCustomerPoints(@PathVariable Long id, @RequestParam Integer points) {
        customerService.updateCustomerPoints(id, points);
    }
}
