package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.PaymentStatus;
import com.example.demo.repository.nguyen.bill.NPaymentStatusRepository;
import com.example.demo.service.nguyen.NPaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NPaymentStatusServiceImpl implements NPaymentStatusService {

    @Autowired
    NPaymentStatusRepository paymentStatusRepository;

    @Override
    public List<PaymentStatus> getAllByBillId(Long billId) {
        return paymentStatusRepository.findAllByBillIdOrderByIdDesc(billId);
    }
}
