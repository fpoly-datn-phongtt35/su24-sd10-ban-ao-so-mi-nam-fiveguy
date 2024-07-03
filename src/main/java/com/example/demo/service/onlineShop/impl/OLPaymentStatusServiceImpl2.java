package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.PaymentStatus;
import com.example.demo.repository.onlineShop.OLPaymentStatusRepository2;
import com.example.demo.service.onlineShop.OLPaymentStatusService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
@Service

public class OLPaymentStatusServiceImpl2 implements OLPaymentStatusService2 {

    @Autowired
    private OLPaymentStatusRepository2 olPaymentStatusRepository2;



    @Override
    public PaymentStatus save(PaymentStatus paymentStatus) {
        String code = generateCode();
        paymentStatus.setCode(code);
        paymentStatus.setPaymentDate(new Date());
        return olPaymentStatusRepository2.save(paymentStatus);
    }

    private String generateCode() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        String formattedTime = formatter.format(new Date(currentTimeMillis));
        return "PS" + formattedTime;
    }
}
