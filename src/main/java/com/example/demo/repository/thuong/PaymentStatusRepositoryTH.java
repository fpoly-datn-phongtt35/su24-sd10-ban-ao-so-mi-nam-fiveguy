package com.example.demo.repository.thuong;

import com.example.demo.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStatusRepositoryTH extends JpaRepository<PaymentStatus, Long> {
    PaymentStatus findByPaymentMethodAndBill_Id(Integer paymentMethod, Long id);
    boolean existsByCode(String code);
}
