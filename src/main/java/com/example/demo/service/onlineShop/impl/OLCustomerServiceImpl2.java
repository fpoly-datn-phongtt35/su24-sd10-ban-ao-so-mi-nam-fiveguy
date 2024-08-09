package com.example.demo.service.onlineShop.impl;

import com.example.demo.repository.onlineShop.OLCustomerRepository2;
import com.example.demo.repository.onlineShop.OlColorRepository2;
import com.example.demo.service.onlineShop.OLCustomerService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OLCustomerServiceImpl2 implements OLCustomerService2 {

    @Autowired
    private OLCustomerRepository2 colorRepository2;

    private static final BigDecimal POINT_CONVERSION_RATE = new BigDecimal(10000);

    @Override
    public int convertAmountToPoints(BigDecimal totalAmount) {
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        return totalAmount.divide(POINT_CONVERSION_RATE).intValue();
    }
}
