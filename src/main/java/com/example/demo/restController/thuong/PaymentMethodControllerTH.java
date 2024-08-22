package com.example.demo.restController.thuong;

import com.example.demo.service.thuong.PaymentMethodServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/payment-method-th")
public class PaymentMethodControllerTH {
    @Autowired
    private PaymentMethodServiceTH paymentMethodService;

    @GetMapping
    public ResponseEntity<?> getPaymentMethods() {
        return ResponseEntity.ok(paymentMethodService.listPaymentMethod());
    }
}
