package com.example.demo.model.request.nguyen;

import com.example.demo.entity.Bill;
import com.example.demo.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentStatusRequest {

    Bill bill;

    PaymentStatus paymentStatus;

    Integer payOrRefund;
}