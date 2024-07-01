package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Image;
import com.example.demo.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OLPaymentMethodRepository2 extends JpaRepository<Image, Long> {

    @Query("SELECT v FROM PaymentMethod v WHERE v.status = 1 AND v.paymentType = 1")
    List<PaymentMethod> findActivePaymentMethods();


}
