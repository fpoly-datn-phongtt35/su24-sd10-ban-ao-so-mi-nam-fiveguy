package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.repository.nguyen.bill.NPaymentStatusRepository;
import com.example.demo.service.nguyen.NPaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public PaymentStatus updateStatusPayment(Long billId, String note) {

        List<PaymentStatus> paymentStatuses = paymentStatusRepository
                .findAllByBillIdAndCustomerPaymentStatusOrderByIdAsc(billId, 1);

        if (paymentStatuses.isEmpty()) return null;

        for (PaymentStatus ps : paymentStatuses){
            if(ps.getCustomerPaymentStatus() == 1 && ps.getPaymentType() == 1){
                ps.setPaymentDate(new Date());
                ps.setCustomerPaymentStatus(2);

                return paymentStatusRepository.save(ps);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public PaymentStatus updateStatusPaymentRefund(Long billId, String note) {

        List<PaymentStatus> paymentStatuses = paymentStatusRepository
                .findAllByBillIdAndCustomerPaymentStatusOrderByIdAsc(billId, 4);

        if (paymentStatuses.isEmpty()) return null;

        for (PaymentStatus ps : paymentStatuses){
            if(ps.getCustomerPaymentStatus() == 4 && ps.getPaymentType() == 3){
                ps.setPaymentDate(new Date());
                ps.setCustomerPaymentStatus(3);

                return paymentStatusRepository.save(ps);
            }
        }
        return null;
    }
}
