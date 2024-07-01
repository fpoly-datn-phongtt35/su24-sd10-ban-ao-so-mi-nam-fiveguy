package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.PaymentMethod;
import com.example.demo.repository.onlineShop.OLPaymentMethodRepository2;
import com.example.demo.service.onlineShop.OLPaymentMethodService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OLPaymentMethodServiceImpl2 implements OLPaymentMethodService2 {

    @Autowired
    private OLPaymentMethodRepository2 olPaymentMethodRepository2;



    @Override
    public List<PaymentMethod> getPaymentMethodsOl() {
        return olPaymentMethodRepository2.findActivePaymentMethods();
    }
}
