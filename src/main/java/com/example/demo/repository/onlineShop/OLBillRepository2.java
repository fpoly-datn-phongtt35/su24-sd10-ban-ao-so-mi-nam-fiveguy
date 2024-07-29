package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OLBillRepository2 extends JpaRepository<Bill, Long> {


    @Query("SELECT b FROM Bill b  WHERE b.customer.id = :customerId AND b.code LIKE %:search%")
    Page<Bill> findByCustomer_Id(Long customerId, String search, Pageable pageable);

    @Query("SELECT b FROM Bill b  WHERE b.phoneNumber = :phoneNumber AND  b.code LIKE %:search%")
    Page<Bill> findByPhoneNumber(String phoneNumber, String search, Pageable pageable);
}


