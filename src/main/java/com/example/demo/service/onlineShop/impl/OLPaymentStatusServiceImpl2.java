package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.PaymentStatus;
import com.example.demo.repository.onlineShop.OLPaymentStatusRepository2;
import com.example.demo.service.onlineShop.OLPaymentStatusService2;
import org.springframework.beans.factory.annotation.Autowired;

public class OLPaymentStatusServiceImpl2 implements OLPaymentStatusService2 {

    @Autowired
    private OLPaymentStatusRepository2 olPaymentStatusRepository2;



    @Override
    public PaymentStatus save(PaymentStatus paymentStatus) {
        return olPaymentStatusRepository2.save(paymentStatus);
    }
}
