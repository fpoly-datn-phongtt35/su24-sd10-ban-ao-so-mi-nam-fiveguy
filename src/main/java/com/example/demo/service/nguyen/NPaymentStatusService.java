package com.example.demo.service.nguyen;

import com.example.demo.entity.PaymentStatus;

import java.util.List;

public interface NPaymentStatusService {

    List<PaymentStatus> getAllByBillId(Long billId);
}
