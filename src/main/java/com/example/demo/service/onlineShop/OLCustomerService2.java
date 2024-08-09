package com.example.demo.service.onlineShop;

import java.math.BigDecimal;

public interface OLCustomerService2 {

    int convertAmountToPoints(BigDecimal totalAmount);

    void updateCustomerPoints(Long customerId, Integer points);
}
