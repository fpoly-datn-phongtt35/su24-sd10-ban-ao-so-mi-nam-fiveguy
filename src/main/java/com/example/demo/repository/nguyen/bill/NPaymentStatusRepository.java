package com.example.demo.repository.nguyen.bill;

import com.example.demo.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NPaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {

    List<PaymentStatus> findAllByBillIdOrderByIdAsc(Long billId);

    List<PaymentStatus> findAllByBillIdAndCustomerPaymentStatusOrderByIdAsc(Long billId, Integer status);

    List<PaymentStatus> findAllByBillIdAndPaymentTypeAndBillStatus(Long billId, Integer paymentType, Integer status);

    boolean existsByCode(String code);
}
