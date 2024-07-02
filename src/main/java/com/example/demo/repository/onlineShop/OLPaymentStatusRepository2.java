package com.example.demo.repository.onlineShop;

import com.example.demo.entity.BillHistory;
import com.example.demo.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OLPaymentStatusRepository2 extends JpaRepository<PaymentStatus, Long> {
}
