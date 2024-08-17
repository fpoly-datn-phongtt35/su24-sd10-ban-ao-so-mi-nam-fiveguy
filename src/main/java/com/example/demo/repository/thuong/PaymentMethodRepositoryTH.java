package com.example.demo.repository.thuong;

import com.example.demo.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepositoryTH extends JpaRepository<PaymentMethod, Long> {
    PaymentMethod findByNameIgnoreCase(String name);
    @Query(value = "SELECT p from PaymentMethod p WHERE p.paymentType = 2 AND p.status = 1")
    List<PaymentMethod> listPaymentMethod();
}
