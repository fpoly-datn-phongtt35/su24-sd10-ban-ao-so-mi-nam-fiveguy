package com.example.demo.repository.thuong;

import com.example.demo.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepositoryTH extends JpaRepository<PaymentMethod, Long> {
    PaymentMethod findByNameIgnoreCase(String name);
}
