package com.example.demo.repository.onlineShop;

import com.example.demo.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OLBillRepository2 extends JpaRepository<Bill, Long> {
}
