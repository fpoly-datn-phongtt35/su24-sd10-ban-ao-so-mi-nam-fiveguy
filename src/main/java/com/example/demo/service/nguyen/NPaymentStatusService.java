package com.example.demo.service.nguyen;

import com.example.demo.entity.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;

public interface NPaymentStatusService {

    List<PaymentStatus> getAllByBillId(Long billId);

    PaymentStatus updateStatusPayment(Long billId, String note);

    PaymentStatus updateStatusPaymentRefund(Long billId, String note);
}
