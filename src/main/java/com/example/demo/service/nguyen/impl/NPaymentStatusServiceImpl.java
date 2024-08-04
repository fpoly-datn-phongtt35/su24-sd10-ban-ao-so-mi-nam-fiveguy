package com.example.demo.service.nguyen.impl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.model.request.nguyen.PaymentStatusRequest;
import com.example.demo.repository.nguyen.bill.NBillRepository;
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

    @Autowired
    NBillRepository billRepository;

    @Override
    public List<PaymentStatus> getAllByBillId(Long billId) {
        return paymentStatusRepository.findAllByBillIdOrderByIdAsc(billId);
    }

    @Override
    @Transactional
    public PaymentStatus createPaymentStatus(Long billId,
                                             PaymentStatusRequest paymentStatusRequest) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (paymentStatusRequest.getPayOrRefund() == 1) {
            bill.setPaidAmount(paymentStatusRequest.getBill().getTotalAmountAfterDiscount());
            bill.setPaidShippingFee(paymentStatusRequest.getBill().getShippingFee());

            billRepository.save(bill);
        } else if (paymentStatusRequest.getPayOrRefund() == 2) {
            bill.setPaidAmount(paymentStatusRequest.getBill().getTotalAmountAfterDiscount());
            bill.setPaidShippingFee(paymentStatusRequest.getBill().getShippingFee());

            billRepository.save(bill);
        } else if (paymentStatusRequest.getPayOrRefund() == 3) {
            bill.setPaidAmount(BigDecimal.ZERO);
            bill.setPaidShippingFee(BigDecimal.ZERO);

            billRepository.save(bill);
        } else if (paymentStatusRequest.getPayOrRefund() == 0) {
            bill.setPaidAmount(paymentStatusRequest.getBill().getTotalAmountAfterDiscount());
            bill.setPaidShippingFee(paymentStatusRequest.getBill().getShippingFee());

            billRepository.save(bill);
        }

        PaymentStatus ps = new PaymentStatus();
        ps.setCode("auto");
        ps.setPaymentDate(new Date());
        ps.setBill(bill);
        ps.setPaymentAmount(paymentStatusRequest.getPaymentStatus().getPaymentAmount());
        ps.setPaymentType(paymentStatusRequest.getPaymentStatus().getPaymentType());
        ps.setPaymentMethod(paymentStatusRequest.getPaymentStatus().getPaymentMethod());
        ps.setNote(paymentStatusRequest.getPaymentStatus().getNote());
        ps.setCustomerPaymentStatus(
                paymentStatusRequest.getPaymentStatus().getCustomerPaymentStatus());

        return paymentStatusRepository.save(ps);
    }


    @Override
    @Transactional
    public PaymentStatus updateStatusPayment(Long billId, String note) {

        List<PaymentStatus> paymentStatuses = paymentStatusRepository
                .findAllByBillIdAndCustomerPaymentStatusOrderByIdAsc(billId, 1);

        if (paymentStatuses.isEmpty()) return null;

        for (PaymentStatus ps : paymentStatuses) {
            if (ps.getCustomerPaymentStatus() == 1 && ps.getPaymentType() == 1) {
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

        for (PaymentStatus ps : paymentStatuses) {
            if (ps.getCustomerPaymentStatus() == 4 && ps.getPaymentType() == 3) {
                ps.setPaymentDate(new Date());
                ps.setCustomerPaymentStatus(3);

                return paymentStatusRepository.save(ps);
            }
        }
        return null;
    }
}
