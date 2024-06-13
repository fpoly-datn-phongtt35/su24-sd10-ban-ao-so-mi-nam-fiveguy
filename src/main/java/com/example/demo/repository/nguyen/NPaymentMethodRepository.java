package com.example.demo.repository.nguyen;

import com.example.demo.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NPaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
