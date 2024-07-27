package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.repository.nguyen.bill.NPaymentStatusRepository;
import com.example.demo.service.nguyen.NPaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class NPaymentStatusServiceImpl implements NPaymentStatusService {

    @Autowired
    NPaymentStatusRepository paymentStatusRepository;

    @Override
    public List<PaymentStatus> getAllByBillId(Long billId) {
        return paymentStatusRepository.findAllByBillIdOrderByIdAsc(billId);
    }

    @Override
    public PaymentStatus updateStatusPayment(Long billId, BigDecimal paymentAmount){

        List<PaymentStatus> paymentStatuses = paymentStatusRepository.findAllByBillIdOrderByIdAsc(billId);

        PaymentStatus paymentStatus = null;
        for (PaymentStatus ps : paymentStatuses){
            if(ps.getPaymentType() == 1){
                paymentStatus = ps;
            }
        }

        if (paymentStatus != null){
            paymentStatus.setPaymentDate(new Date());
            paymentStatus.setCustomerPaymentStatus(2);

            return paymentStatusRepository.save(paymentStatus);
        }
        return null;
    }
}
