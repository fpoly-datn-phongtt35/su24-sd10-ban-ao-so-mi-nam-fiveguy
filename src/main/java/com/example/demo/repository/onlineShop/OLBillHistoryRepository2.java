package com.example.demo.repository.onlineShop;

import com.example.demo.entity.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OLBillHistoryRepository2 extends JpaRepository<BillHistory, Long> {
}
