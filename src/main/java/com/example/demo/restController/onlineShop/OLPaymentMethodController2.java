package com.example.demo.restController.onlineShop;

import com.example.demo.service.onlineShop.OLPaymentMethodService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/home")
public class OLPaymentMethodController2 {

    @Autowired
    private OLPaymentMethodService2 olPaymentMethodService;

    @GetMapping("/paymentMethods")
    public ResponseEntity<?> findAllVouchers() {
        return ResponseEntity.ok(olPaymentMethodService.getPaymentMethodsOl());
    }



}
