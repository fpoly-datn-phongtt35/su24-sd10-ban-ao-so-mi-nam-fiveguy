package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.entity.PaymentMethod;
import com.example.demo.repository.thuong.PaymentMethodRepositoryTH;
import com.example.demo.service.thuong.PaymentMethodServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodServiceTHImpl implements PaymentMethodServiceTH {

    @Autowired
    private PaymentMethodRepositoryTH paymentMethodRepository;

    @Override
    public List<PaymentMethod> listPaymentMethod() {
        return paymentMethodRepository.listPaymentMethod();
    }
}
